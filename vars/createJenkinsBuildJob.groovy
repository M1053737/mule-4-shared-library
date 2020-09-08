import com.mulesoft.Constants
import com.mulesoft.PipelinePlaceholders
import com.mulesoft.Secrets
import groovy.json.JsonSlurperClassic

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

    def authString = "admin:${secrets.getSecret('jenkins-api-token')}".getBytes().encodeBase64().toString()
    echo authString
          
    def folderName=pipelinePlaceholders.getEnvironment()
    echo folderName
    
    //def folderName=pipelinePlaceholders.getEnvironment()
    echo "Jenkins"
    //echo folderName
    echo jobName
    echo Constants.JENKINS_DOMAIN
    
    def payload = readFile "buildConfig.xml"
    echo payload
                    
    def response = httpRequest (
        httpMode: "POST",
        url: "http://${Constants.JENKINS_DOMAIN}/createItem?name=${jobName}",
      // url: "http://52.172.43.67:8080/job/${folderName}/createItem?name=${jobName}",
        requestBody: payload,
        customHeaders: [[name: 'Authorization', value: "Basic ${authString}"],[name: 'Content-Type', value: 'application/xml']]
        quiet: true        
     )
    
    sh "rm buildConfig.xml"
}
