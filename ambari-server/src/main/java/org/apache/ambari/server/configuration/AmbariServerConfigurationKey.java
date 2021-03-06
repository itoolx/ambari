/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.ambari.server.configuration;

import static org.apache.ambari.server.configuration.ConfigurationPropertyType.PASSWORD;
import static org.apache.ambari.server.configuration.ConfigurationPropertyType.PLAINTEXT;

/**
 * Constants representing supported LDAP related property names
 */
public enum AmbariServerConfigurationKey {

  /* ********************************************************
   * LDAP Configuration Keys
   * ******************************************************** */
  LDAP_ENABLED(AmbariServerConfigurationCategory.LDAP_CONFIGURATION, "ambari.ldap.authentication.enabled", PLAINTEXT, "false", "An internal property used for unit testing and development purposes."),
  SERVER_HOST(AmbariServerConfigurationCategory.LDAP_CONFIGURATION, "ambari.ldap.connectivity.server.host", PLAINTEXT, "localhost", "The LDAP URL host used for connecting to an LDAP server when authenticating users."),
  SERVER_PORT(AmbariServerConfigurationCategory.LDAP_CONFIGURATION, "ambari.ldap.connectivity.server.port", PLAINTEXT, "33389", "The LDAP URL port used for connecting to an LDAP server when authenticating users."),
  SECONDARY_SERVER_HOST(AmbariServerConfigurationCategory.LDAP_CONFIGURATION, "ambari.ldap.connectivity.secondary.server.host", PLAINTEXT, null, "A second LDAP URL host to use as a backup when authenticating users."),
  SECONDARY_SERVER_PORT(AmbariServerConfigurationCategory.LDAP_CONFIGURATION, "ambari.ldap.connectivity.secondary.server.port", PLAINTEXT, null, "A second LDAP URL port to use as a backup when authenticating users."),
  USE_SSL(AmbariServerConfigurationCategory.LDAP_CONFIGURATION, "ambari.ldap.connectivity.use_ssl", PLAINTEXT, "false", "Determines whether to use LDAP over SSL (LDAPS)."),

  TRUST_STORE(AmbariServerConfigurationCategory.LDAP_CONFIGURATION, "ambari.ldap.connectivity.trust_store", PLAINTEXT, "", ""), //TODO
  TRUST_STORE_TYPE(AmbariServerConfigurationCategory.LDAP_CONFIGURATION, "ambari.ldap.connectivity.trust_store.type", PLAINTEXT, null, "The type of truststore used by the 'javax.net.ssl.trustStoreType' property."),
  TRUST_STORE_PATH(AmbariServerConfigurationCategory.LDAP_CONFIGURATION, "ambari.ldap.connectivity.trust_store.path", PLAINTEXT, null, "The location of the truststore to use when setting the 'javax.net.ssl.trustStore' property."),
  TRUST_STORE_PASSWORD(AmbariServerConfigurationCategory.LDAP_CONFIGURATION, "ambari.ldap.connectivity.trust_store.password", PASSWORD, null, "The password to use when setting the 'javax.net.ssl.trustStorePassword' property"),
  ANONYMOUS_BIND(AmbariServerConfigurationCategory.LDAP_CONFIGURATION, "ambari.ldap.connectivity.anonymous_bind", PLAINTEXT, "true", "Determines whether LDAP requests can connect anonymously or if a managed user is required to connect."),

  BIND_DN(AmbariServerConfigurationCategory.LDAP_CONFIGURATION, "ambari.ldap.connectivity.bind_dn", PLAINTEXT, null, "The DN of the manager account to use when binding to LDAP if anonymous binding is disabled."),
  BIND_PASSWORD(AmbariServerConfigurationCategory.LDAP_CONFIGURATION, "ambari.ldap.connectivity.bind_password", PASSWORD, null, "The password for the manager account used to bind to LDAP if anonymous binding is disabled."),

  ATTR_DETECTION(AmbariServerConfigurationCategory.LDAP_CONFIGURATION, "ambari.ldap.attributes.detection", PLAINTEXT, "", ""), //TODO

  DN_ATTRIBUTE(AmbariServerConfigurationCategory.LDAP_CONFIGURATION, "ambari.ldap.attributes.dn_attr", PLAINTEXT, "dn", "The attribute used for determining what the distinguished name property is."),

  USER_OBJECT_CLASS(AmbariServerConfigurationCategory.LDAP_CONFIGURATION, "ambari.ldap.attributes.user.object_class", PLAINTEXT, "person", "The class to which user objects in LDAP belong."),
  USER_NAME_ATTRIBUTE(AmbariServerConfigurationCategory.LDAP_CONFIGURATION, "ambari.ldap.attributes.user.name_attr", PLAINTEXT, "uid", "The attribute used for determining the user name, such as 'uid'."),
  USER_GROUP_MEMBER_ATTRIBUTE(AmbariServerConfigurationCategory.LDAP_CONFIGURATION, "ambari.ldap.attributes.user.group_member_attr", PLAINTEXT, "", ""), //TODO
  USER_SEARCH_BASE(AmbariServerConfigurationCategory.LDAP_CONFIGURATION, "ambari.ldap.attributes.user.search_base", PLAINTEXT, "dc=ambari,dc=apache,dc=org", "The base DN to use when filtering LDAP users and groups. This is only used when LDAP authentication is enabled."),
  USER_BASE(AmbariServerConfigurationCategory.LDAP_CONFIGURATION, "ambari.ldap.attributes.search_user_base", PLAINTEXT, "ou=people,dc=ambari,dc=apache,dc=org", "The filter used when searching for users in LDAP."),

  GROUP_OBJECT_CLASS(AmbariServerConfigurationCategory.LDAP_CONFIGURATION, "ambari.ldap.attributes.group.object_class", PLAINTEXT, "ou=groups,dc=ambari,dc=apache,dc=org", "The filter used when searching for groups in LDAP."),
  GROUP_NAME_ATTRIBUTE(AmbariServerConfigurationCategory.LDAP_CONFIGURATION, "ambari.ldap.attributes.group.name_attr", PLAINTEXT, "cn", "The attribute used to determine the group name in LDAP."),
  GROUP_MEMBER_ATTRIBUTE(AmbariServerConfigurationCategory.LDAP_CONFIGURATION, "ambari.ldap.attributes.group.member_attr", PLAINTEXT, "member", "The LDAP attribute which identifies group membership."),
  GROUP_SEARCH_BASE(AmbariServerConfigurationCategory.LDAP_CONFIGURATION, "ambari.ldap.attributes.group.search_base", PLAINTEXT, "dc=ambari,dc=apache,dc=org", "The base DN to use when filtering LDAP users and groups. This is only used when LDAP authentication is enabled."),
  GROUP_BASE(AmbariServerConfigurationCategory.LDAP_CONFIGURATION, "ambari.ldap.attributes.group.search_group_base", PLAINTEXT, "ou=groups,dc=ambari,dc=apache,dc=org", "The filter used when searching for groups in LDAP."),

  USER_SEARCH_FILTER(AmbariServerConfigurationCategory.LDAP_CONFIGURATION, "ambari.ldap.advanced.user_search_filter", PLAINTEXT, "(&({usernameAttribute}={0})(objectClass={userObjectClass}))", "A filter used to lookup a user in LDAP based on the Ambari user name."),
  USER_MEMBER_REPLACE_PATTERN(AmbariServerConfigurationCategory.LDAP_CONFIGURATION, "ambari.ldap.advanced.user_member_replace_pattern", PLAINTEXT, "", "Regex pattern to use when replacing the user member attribute ID value with a placeholder. This is used in cases where a UID of an LDAP member is not a full CN or unique ID (e.g.: 'member: <SID=123>;<GID=123>;cn=myCn,dc=org,dc=apache')"),
  USER_MEMBER_FILTER(AmbariServerConfigurationCategory.LDAP_CONFIGURATION, "ambari.ldap.advanced.user_member_filter", PLAINTEXT, "", "Filter to use for syncing user members of a group from LDAP (by default it is not used). For example: (&(objectclass=posixaccount)(uid={member}))"),

  ALTERNATE_USER_SEARCH_ENABLED(AmbariServerConfigurationCategory.LDAP_CONFIGURATION, "ambari.ldap.advanced.alternate_user_search_enabled", PLAINTEXT, "false", "Determines whether a secondary (alternate) LDAP user search filer is used if the primary filter fails to find a user."),
  ALTERNATE_USER_SEARCH_FILTER(AmbariServerConfigurationCategory.LDAP_CONFIGURATION, "ambari.ldap.advanced.alternate_user_search_filter", PLAINTEXT, "(&(userPrincipalName={0})(objectClass={userObjectClass}))", "An alternate LDAP user search filter which can be used if 'authentication.ldap.alternateUserSearchEnabled' is enabled and the primary filter fails to find a user."),

  GROUP_SEARCH_FILTER(AmbariServerConfigurationCategory.LDAP_CONFIGURATION, "ambari.ldap.advanced.group_search_filter", PLAINTEXT, "", "The DN to use when searching for LDAP groups."),
  GROUP_MEMBER_REPLACE_PATTERN(AmbariServerConfigurationCategory.LDAP_CONFIGURATION, "ambari.ldap.advanced.group_member_replace_pattern", PLAINTEXT, "", "Regex pattern to use when replacing the group member attribute ID value with a placeholder. This is used in cases where a UID of an LDAP member is not a full CN or unique ID (e.g.: 'member: <SID=123>;<GID=123>;cn=myCn,dc=org,dc=apache')"),
  GROUP_MEMBER_FILTER(AmbariServerConfigurationCategory.LDAP_CONFIGURATION, "ambari.ldap.advanced.group_member_filter", PLAINTEXT, "", "Filter to use for syncing group members of a group from LDAP. (by default it is not used). For example: (&(objectclass=posixgroup)(cn={member}))"),
  GROUP_MAPPING_RULES(AmbariServerConfigurationCategory.LDAP_CONFIGURATION, "ambari.ldap.advanced.group_mapping_rules", PLAINTEXT, "Ambari Administrators", "A comma-separate list of groups which would give a user administrative access to Ambari when syncing from LDAP. This is only used when 'authorization.ldap.groupSearchFilter' is blank. For instance: Hadoop Admins, Hadoop Admins.*, DC Admins, .*Hadoop Operators"),

  FORCE_LOWERCASE_USERNAMES(AmbariServerConfigurationCategory.LDAP_CONFIGURATION, "ambari.ldap.advanced.force_lowercase_usernames", PLAINTEXT, "", "Declares whether to force the ldap user name to be lowercase or leave as-is.\nThis is useful when local user names are expected to be lowercase but the LDAP user names are not."),
  REFERRAL_HANDLING(AmbariServerConfigurationCategory.LDAP_CONFIGURATION, "ambari.ldap.advanced.referrals", PLAINTEXT, "follow", "Determines whether to follow LDAP referrals to other URLs when the LDAP controller doesn't have the requested object."),
  PAGINATION_ENABLED(AmbariServerConfigurationCategory.LDAP_CONFIGURATION, "ambari.ldap.advanced.pagination_enabled", PLAINTEXT, "true", "Determines whether results from LDAP are paginated when requested."),
  COLLISION_BEHAVIOR(AmbariServerConfigurationCategory.LDAP_CONFIGURATION, "ambari.ldap.advance.collision_behavior", PLAINTEXT, "convert", "Determines how to handle username collision while updating from LDAP."),

  /* ********************************************************
   * SSO Configuration Keys
   * ******************************************************** */
  SSO_MANAGE_SERVICES(AmbariServerConfigurationCategory.SSO_CONFIGURATION, "ambari.sso.manage_services", PLAINTEXT, "false", "A Boolean value indicating whether Ambari is to manage the SSO configuration for services or not."),
  SSO_ENABED_SERVICES(AmbariServerConfigurationCategory.SSO_CONFIGURATION, "ambari.sso.enabled_services", PLAINTEXT, null, "A comma-delimited list of services that are expected to be configured for SSO.  A \"*\" indicates all services.");

  private final AmbariServerConfigurationCategory configurationCategory;
  private final String propertyName;
  private final ConfigurationPropertyType configurationPropertyType;
  private final String defaultValue;
  private final String description;

  AmbariServerConfigurationKey(AmbariServerConfigurationCategory configurationCategory, String propName, ConfigurationPropertyType configurationPropertyType, String defaultValue, String description) {
    this.configurationCategory = configurationCategory;
    this.propertyName = propName;
    this.configurationPropertyType = configurationPropertyType;
    this.defaultValue = defaultValue;
    this.description = description;
  }

  public AmbariServerConfigurationCategory getConfigurationCategory() {
    return configurationCategory;
  }

  public String key() {
    return this.propertyName;
  }

  public ConfigurationPropertyType getConfigurationPropertyType() {
    return configurationPropertyType;
  }

  public String getDefaultValue() {
    return defaultValue;
  }

  public String getDescription() {
    return description;
  }

  public static AmbariServerConfigurationKey translate(AmbariServerConfigurationCategory category, String keyName) {
    for (AmbariServerConfigurationKey key : values()) {
      if (key.configurationCategory.equals(category) && key.propertyName.equals(keyName)) {
        return key;
      }
    }

    String categoryName = (category == null) ? "null" : category.getCategoryName();
    throw new IllegalArgumentException(String.format("Invalid Ambari server configuration key: %s:%s", categoryName, keyName));

  }
}
