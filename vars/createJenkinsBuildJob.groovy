import com.mulesoft.Constants
import com.mulesoft.PipelinePlaceholders
import com.mulesoft.Secrets

def call() {
    pipelinePlaceholders = PipelinePlaceholders.getInstance()
    secrets = Secrets.getInstance()
    def jobName

    if(pipelinePlaceholders.getApiAssetId()) { // IMPLIES THIS IS AN API PROJECT
        jobName = "(${pipelinePlaceholders.getDeploymentType()})-${pipelinePlaceholders.getApiAssetIdFormatted()}-(build)"  
    }
    else { // IMPLIES THIS IS A DOMAIN PROJECT
        jobName = "(${pipelinePlaceholders.getDeploymentType()})-${pipelinePlaceholders.getDomainNameFormatted()}-(build)"
    }

    def authString = "root:${secrets.getSecret('jenkins-api-token')}".getBytes().encodeBase64().toString()
    echo authString
    
    def folderName=pipelinePlaceholders.getEnvironment()
    echo folderName

    def payload = readFile "buildConfig.xml"

    def response = httpRequest (
        httpMode: "POST",
        //url: "https://${Constants.JENKINS_DOMAIN}/createItem?name=${jobName}",
        url: "https://${Constants.JENKINS_DOMAIN}/job/${folderName}/createItem?name=${jobName}",
        requestBody: payload,
        customHeaders: [[name: 'Authorization', value: "Basic ${authString}"], [name: 'Content-Type', value: 'application/xml']],
        quiet: true
    )

    sh "rm buildConfig.xml"
}
