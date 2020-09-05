
import com.mulesoft.Constants
import com.mulesoft.PipelinePlaceholders

def call() {
    pipelinePlaceholders = PipelinePlaceholders.getInstance()
    def jobName

    if(pipelinePlaceholders.getApiAssetId()) { // IMPLIES THIS IS AN API PROJECT
        jobName = "(${pipelinePlaceholders.getDeploymentType()})-${pipelinePlaceholders.getApiAssetIdFormatted()}-(deploy)"
    }

    def authString = "jenkins:${pipelinePlaceholders.getVaultSecret('jenkins-api-token')}".getBytes().encodeBase64().toString()

    def crumbResponse = httpRequest (
        httpMode: "GET",
        url: "http://${Constants.JENKINS_DOMAIN}/crumbIssuer/api/json",
        customHeaders: [[name: 'Authorization', value: "Basic ${authString}"]],
        quiet: true
    )   
    def crumbResponseMap = new groovy.json.JsonSlurperClassic().parseText(crumbResponse.content)

    def payload = readFile "deployConfig.xml"

    def response = httpRequest (
        httpMode: "POST",
        url: "http://${Constants.JENKINS_DOMAIN}/createItem?name=${jobName}",
        requestBody: payload,
        customHeaders: [[name: 'Authorization', value: "Basic ${authString}"], [name: 'Content-Type', value: 'application/xml'], [name: crumbResponseMap.crumbRequestField, value: crumbResponseMap.crumb]],
        quiet: true
    )

    sh "rm deployConfig.xml"
}
