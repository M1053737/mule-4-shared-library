package com.mulesoft

@Singleton
public class PipelinePlaceholders {

    // DEPLOYMENT TYPE CONTAINER ------------------------------------------------------------------------------------------
    private String gitlabGroupNamespaceId
    private String deploymentType

    public String getDeploymentType() {
        return deploymentType;
    }

    public void setDeploymentType(String deploymentType) {
        this.deploymentType = deploymentType;
        
        if(deploymentType == "cloudhub") { 
            gitlabGroupNamespaceId = "11";
        } else if(deploymentType == "rtf") { 
            gitlabGroupNamespaceId = "14";
        } else if(deploymentType == "onprem-docker") { 
            gitlabGroupNamespaceId = "10";
        } else if(deploymentType == "onprem-domain") { 
            gitlabGroupNamespaceId = "9";
        } else if(deploymentType == "onprem-standalone") { 
            gitlabGroupNamespaceId = "8";
        }
    } 

    public String getGitlabGroupNamespaceId() {
        return gitlabGroupNamespaceId;
    }

    // AUTHORIZATION TOKEN CONTAINER ------------------------------------------------------------------------------------------

    private String authorizationToken

    public String getAuthorizationToken() {
        return authorizationToken;
    }

    public void setAuthorizationToken(String authorizationToken) {
        this.authorizationToken = authorizationToken;
    }   

    // REPOSITORY DATA CONTAINER ----------------------------------------------------------------------------------------------

    private String sshUrlToRepo
    private String httpUrlToRepo

    public String getSshUrlToRepo() {
        return sshUrlToRepo;
    }

    public void setSshUrlToRepo(String sshUrlToRepo) {
        this.sshUrlToRepo = sshUrlToRepo;
    }   

    public String getHttpUrlToRepo() {
        return httpUrlToRepo;
    }

    public void setHttpUrlToRepo(String httpUrlToRepo) {
        this.httpUrlToRepo = httpUrlToRepo;
    }

    // DOMAIN NAME CONTAINER --------------------------------------------------------------------------------------------------
    private String domainName
    private String domainDependency

    public void setDomainDependency(String domainDependency) {
        this.domainDependency = domainDependency.replaceAll("\n", "\\\\n") + "<classifier>mule-domain</classifier>\\\\n<scope>provided</scope>"
    }

    public String getDomainDependency() {
        return this.domainDependency
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName
    }

    public String getDomainName() {
        return this.domainName
    }

    public String getDomainNameFormatted() {
        return this.domainName.toLowerCase().replaceAll("\\s","-")
    }

    // PORTS CONTAINER --------------------------------------------------------------------------------------------------------
    private Map ports = [:]
   
    public String getPorts() {
        return this.ports
    }

    public void setPorts(Map ports) {
        this.ports = ports;
    }

    // ORGANIZATION CONTAINER -------------------------------------------------------------------------------------------------
    private String organizationId
    private String organization
 
    public String getOrganization() {
        return organization;
    }

    public String getOrganizationFormatted() {
        return organization.toLowerCase().replaceAll("\\s","-")
    }

    public String getOrganizationNoSpaces() {
        return organization.replaceAll("\\s","_")
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    // ANYPOINT STRUCTURE CONTAINER -------------------------------------------------------------------------------------------------
    private Map apStructure

    public Map getApStructure() {
        return this.apStructure
    }

    public void setApStructure(Map apStructure) {
        this.apStructure = apStructure
    }

    // ENVIRONMENTS CONTAINER -------------------------------------------------------------------------------------------------
    private Map environments = [:]
    private String environment

    public String getEnvironmentId(String environment) {
        return this.environments.get(environment)
    }

    public void setEnvironments(Map environments) {
        this.environments = environments
    }

    public Map getEnvironments() {
        return this.environments
    }
    
     public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }
    
    //REST Template reference URL CONTAINER -------------------------------------------------------------------------------------------------
    private String restUrl
    
    public String getRestTempUrl() {
        return restUrl;
    }
    public void setRestTempUrl(String restUrl) {
        this.restUrl = restUrl;
    }
    
  

    // DEPLOYMENT TARGETS CONTAINER -------------------------------------------------------------------------------------------
    private Map deploymentTargets = [:]

    public void setDeploymentTargets(deploymentTargets) {
        this.deploymentTargets = deploymentTargets;
    }

    public String getDeploymentTargetId(environment, targetName) {
        def deploymentTargets = this.deploymentTargets[environment]
        def deploymentTargetId = deploymentTargets[targetName]
        return deploymentTargetId
    }

    // MVN (ARTIFACT) CONTAINER --------------------------------------------------------------------------------------------------------
    private String mvnArtifactId
    private String mvnGroupId
    private String mvnArtifactVersion

    public String getMvnArtifactId() {
        return mvnArtifactId;
    }

    public void setMvnArtifactId(String mvnArtifactId) {
        this.mvnArtifactId = mvnArtifactId;
    }

    public String getMvnGroupId() {
        return mvnGroupId;
    }

    public void setMvnGroupId(String mvnGroupId) {
        this.mvnGroupId = mvnGroupId;
    }

    public String getMvnArtifactVersion() {
        return mvnArtifactVersion;
    }

    public void setMvnArtifactVersion(String mvnArtifactVersion) {
        this.mvnArtifactVersion = mvnArtifactVersion;
    }

    // ASSET CONTAINER --------------------------------------------------------------------------------------------------------
    private String apiAssetId
    private String apiAssetVersion

    public String getApiAssetId() {
        return apiAssetId;
    }

    public String getApiAssetIdFormatted() {
        return apiAssetId.toLowerCase().replaceAll("\\s","-");
    }

    public void setApiAssetId(String apiAssetId) {
        this.apiAssetId = apiAssetId;
    }

    public String getApiAssetVersion() {
        return apiAssetVersion;
    }

    public void setApiAssetVersion(String apiAssetVersion) {
        this.apiAssetVersion = apiAssetVersion;
    }

    // AUTODISCOVERY DETAILS CONTAINER ----------------------------------------------------------------------------------------
    private Map autoDiscoveryApiNames = [:]
    private Map autoDiscoveryApiVersions = [:]

    private void setAutoDiscoveryApiName(String environmentName, String autoDiscoveryApiName) {
        this.autoDiscoveryApiNames.put(environmentName, autoDiscoveryApiName)
    }

    private void setAutoDiscoveryApiVersion(String environmentName, String autoDiscoveryApiVersion) {
        this.autoDiscoveryApiVersions.put(environmentName, autoDiscoveryApiVersion)
    }

    private String getAutoDiscoveryApiName(String environmentName) {
        return autoDiscoveryApiNames.get(environmentName)
    }

    private String getAutoDiscoveryApiVersion(String environmentName) {
        return autoDiscoveryApiVersions.get(environmentName)
    }

    // NEW 
    private String apiAutoDiscoveryId

    private void setApiAutoDiscoveryId(String apiAutoDiscoveryId) {
        this.apiAutoDiscoveryId = apiAutoDiscoveryId
    }

    private String getApiAutoDiscoveryId() {
        return this.apiAutoDiscoveryId
    }

    // vaultSecrets Container 
    private Map vaultSecrets = [:]

    private void setVaultSecrets(Map vaultSecrets) {
        this.vaultSecrets = vaultSecrets
    }

    private Map getVaultSecrets() {
        return this.vaultSecrets
    }

    private String getVaultSecret(String key) {
        return this.vaultSecrets[key]
    }

    private void removeVaultSecrets() {
        this.vaultSecrets = [:]
    }

}
