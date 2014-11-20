/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.ambari.server.controller.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ambari.server.StaticallyInject;
import org.apache.ambari.server.controller.spi.NoSuchParentResourceException;
import org.apache.ambari.server.controller.spi.NoSuchResourceException;
import org.apache.ambari.server.controller.spi.Predicate;
import org.apache.ambari.server.controller.spi.Request;
import org.apache.ambari.server.controller.spi.RequestStatus;
import org.apache.ambari.server.controller.spi.Resource;
import org.apache.ambari.server.controller.spi.ResourceAlreadyExistsException;
import org.apache.ambari.server.controller.spi.SystemException;
import org.apache.ambari.server.controller.spi.UnsupportedPropertyException;
import org.apache.ambari.server.controller.spi.Resource.Type;
import org.apache.ambari.server.controller.utilities.PropertyHelper;
import org.apache.ambari.server.orm.dao.ClusterVersionDAO;
import org.apache.ambari.server.orm.dao.HostVersionDAO;
import org.apache.ambari.server.orm.dao.RepositoryVersionDAO;
import org.apache.ambari.server.orm.entities.ClusterVersionEntity;
import org.apache.ambari.server.orm.entities.HostVersionEntity;
import org.apache.ambari.server.orm.entities.RepositoryVersionEntity;
import org.apache.ambari.server.state.RepositoryVersionState;

import com.google.inject.Inject;

/**
 * Resource provider for cluster stack versions resources.
 */
@StaticallyInject
public class ClusterStackVersionResourceProvider extends AbstractResourceProvider {

  // ----- Property ID constants ---------------------------------------------

  protected static final String CLUSTER_STACK_VERSION_ID_PROPERTY_ID              = PropertyHelper.getPropertyId("ClusterStackVersions", "id");
  protected static final String CLUSTER_STACK_VERSION_CLUSTER_NAME_PROPERTY_ID    = PropertyHelper.getPropertyId("ClusterStackVersions", "cluster_name");
  protected static final String CLUSTER_STACK_VERSION_STACK_PROPERTY_ID           = PropertyHelper.getPropertyId("ClusterStackVersions", "stack");
  protected static final String CLUSTER_STACK_VERSION_VERSION_PROPERTY_ID         = PropertyHelper.getPropertyId("ClusterStackVersions", "version");
  protected static final String CLUSTER_STACK_VERSION_STATE_PROPERTY_ID           = PropertyHelper.getPropertyId("ClusterStackVersions", "state");
  protected static final String CLUSTER_STACK_VERSION_INSTALLED_HOSTS_PROPERTY_ID = PropertyHelper.getPropertyId("ClusterStackVersions", "installed_hosts");
  protected static final String CLUSTER_STACK_VERSION_CURRENT_HOSTS_PROPERTY_ID   = PropertyHelper.getPropertyId("ClusterStackVersions", "current_hosts");
  protected static final String CLUSTER_STACK_VERSION_REPOSITORIES_PROPERTY_ID    = PropertyHelper.getPropertyId("ClusterStackVersions", "repositories");

  @SuppressWarnings("serial")
  private static Set<String> pkPropertyIds = new HashSet<String>() {
    {
      add(CLUSTER_STACK_VERSION_CLUSTER_NAME_PROPERTY_ID);
      add(CLUSTER_STACK_VERSION_ID_PROPERTY_ID);
    }
  };

  @SuppressWarnings("serial")
  private static Set<String> propertyIds = new HashSet<String>() {
    {
      add(CLUSTER_STACK_VERSION_ID_PROPERTY_ID);
      add(CLUSTER_STACK_VERSION_CLUSTER_NAME_PROPERTY_ID);
      add(CLUSTER_STACK_VERSION_STACK_PROPERTY_ID);
      add(CLUSTER_STACK_VERSION_VERSION_PROPERTY_ID);
      add(CLUSTER_STACK_VERSION_STATE_PROPERTY_ID);
      add(CLUSTER_STACK_VERSION_INSTALLED_HOSTS_PROPERTY_ID);
      add(CLUSTER_STACK_VERSION_CURRENT_HOSTS_PROPERTY_ID);
      add(CLUSTER_STACK_VERSION_REPOSITORIES_PROPERTY_ID);
    }
  };

  @SuppressWarnings("serial")
  private static Map<Type, String> keyPropertyIds = new HashMap<Type, String>() {
    {
      put(Resource.Type.Cluster, CLUSTER_STACK_VERSION_CLUSTER_NAME_PROPERTY_ID);
      put(Resource.Type.ClusterStackVersion, CLUSTER_STACK_VERSION_ID_PROPERTY_ID);
    }
  };

  @Inject
  private static ClusterVersionDAO clusterVersionDAO;

  @Inject
  private static HostVersionDAO hostVersionDAO;

  @Inject
  private static RepositoryVersionDAO repositoryVersionDAO;

  /**
   * Constructor.
   */
  public ClusterStackVersionResourceProvider() {
    super(propertyIds, keyPropertyIds);
  }

  @Override
  public Set<Resource> getResources(Request request, Predicate predicate) throws
      SystemException, UnsupportedPropertyException, NoSuchResourceException, NoSuchParentResourceException {
    final Set<Resource> resources = new HashSet<Resource>();
    final Set<String> requestedIds = getRequestPropertyIds(request, predicate);
    final Set<Map<String, Object>> propertyMaps = getPropertyMaps(predicate);

    List<ClusterVersionEntity> requestedEntities = new ArrayList<ClusterVersionEntity>();
    for (Map<String, Object> propertyMap: propertyMaps) {
      final String clusterName = propertyMap.get(CLUSTER_STACK_VERSION_CLUSTER_NAME_PROPERTY_ID).toString();
      final Long id;
      if (propertyMap.get(CLUSTER_STACK_VERSION_ID_PROPERTY_ID) == null && propertyMaps.size() == 1) {
        requestedEntities = clusterVersionDAO.findByCluster(clusterName);
      } else {
        try {
          id = Long.parseLong(propertyMap.get(CLUSTER_STACK_VERSION_ID_PROPERTY_ID).toString());
        } catch (Exception ex) {
          throw new SystemException("Stack version should have numerical id");
        }
        final ClusterVersionEntity entity = clusterVersionDAO.findByPK(id);
        if (entity == null) {
          throw new NoSuchResourceException("There is no stack version with id " + id);
        } else {
          requestedEntities.add(entity);
        }
      }
    }

    for (ClusterVersionEntity entity: requestedEntities) {
      final Resource resource = new ResourceImpl(Resource.Type.ClusterStackVersion);
      final RepositoryVersionEntity repositoryVersionEntity = repositoryVersionDAO.findByStackAndVersion(entity.getStack(), entity.getVersion());

      final Map<RepositoryVersionState, List<String>> hosts = new HashMap<RepositoryVersionState, List<String>>();
      for (RepositoryVersionState state: RepositoryVersionState.values()) {
        hosts.put(state, new ArrayList<String>());
      }
      for (HostVersionEntity hostVersionEntity: hostVersionDAO.findByClusterStackAndVersion(entity.getClusterEntity().getClusterName(), entity.getStack(), entity.getVersion())) {
        hosts.get(hostVersionEntity.getState()).add(hostVersionEntity.getHostName());
      }

      setResourceProperty(resource, CLUSTER_STACK_VERSION_CLUSTER_NAME_PROPERTY_ID, entity.getClusterEntity().getClusterName(), requestedIds);

      setResourceProperty(resource, CLUSTER_STACK_VERSION_CURRENT_HOSTS_PROPERTY_ID, hosts.get(RepositoryVersionState.CURRENT), requestedIds);
      setResourceProperty(resource, CLUSTER_STACK_VERSION_ID_PROPERTY_ID, entity.getId(), requestedIds);
      setResourceProperty(resource, CLUSTER_STACK_VERSION_INSTALLED_HOSTS_PROPERTY_ID, hosts.get(RepositoryVersionState.INSTALLED), requestedIds);
      if (repositoryVersionEntity != null) {
        setResourceProperty(resource, CLUSTER_STACK_VERSION_REPOSITORIES_PROPERTY_ID, repositoryVersionEntity.getRepositories(), requestedIds);
      }
      setResourceProperty(resource, CLUSTER_STACK_VERSION_STACK_PROPERTY_ID, entity.getStack(), requestedIds);
      setResourceProperty(resource, CLUSTER_STACK_VERSION_STATE_PROPERTY_ID, entity.getState().name(), requestedIds);
      setResourceProperty(resource, CLUSTER_STACK_VERSION_VERSION_PROPERTY_ID, entity.getVersion(), requestedIds);

      if (predicate == null || predicate.evaluate(resource)) {
        resources.add(resource);
      }
    }
    return resources;
  }

  @Override
  public RequestStatus createResources(Request request) throws SystemException,
      UnsupportedPropertyException, ResourceAlreadyExistsException,
      NoSuchParentResourceException {
    throw new SystemException("Method not supported");
  }

  @Override
  public RequestStatus updateResources(Request request, Predicate predicate)
      throws SystemException, UnsupportedPropertyException,
      NoSuchResourceException, NoSuchParentResourceException {
    throw new SystemException("Method not supported");
  }

  @Override
  public RequestStatus deleteResources(Predicate predicate)
      throws SystemException, UnsupportedPropertyException,
      NoSuchResourceException, NoSuchParentResourceException {
    throw new SystemException("Method not supported");
  }

  @Override
  protected Set<String> getPKPropertyIds() {
    return pkPropertyIds;
  }
}
