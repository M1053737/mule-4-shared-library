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
    //def authString = "root:${secrets.getSecret('jenkins-api-token')}"
    echo authString
    //echo ${Constants.JENKINS_DOMAIN}
    //echo ${folderName}
    //echo ${jobName}
    
    def folderName=pipelinePlaceholders.getEnvironment()
    echo "Jenkins"
    echo folderName
    echo jobName
    echo Constants.JENKINS_DOMAIN

    def payload = readFile "buildConfig.xml"
    
      

    def response = httpRequest (
        httpMode: "POST",
       // url: "http://${Constants.JENKINS_DOMAIN}/createItem?name=${jobName}",
        url: "http://52.172.43.67:8080/createItem?name=NEWJOB4534222321",
        //url: "http://${Constants.JENKINS_DOMAIN}/job/${folderName}/createItem?name=${jobName}",
        requestBody: payload,
        //customHeaders: [[name: 'Authorization', value: "Token ${authString}"], [name: 'Content-Type', value: 'application/xml']],
        //customHeaders: [[name: 'Authorization', value: "Basic YWRtaW46YWRtaW4xMjM="], [name: 'Content-Type', value: 'application/xml']],
        customHeaders: [[name: 'Authorization', value: "Basic YWRtaW46YWRtaW4xMjM="], [name: 'Content-Type', value: 'application/xml'], [name: 'Jenkins-Crumb', value: "b7c7240355e7023838e30d0db556822c6906d9b58f6d33e7eed0ee8b0421099d"]],
        quiet: true
        //validResponseCodes: '200:408'
   
    )
    
    sh "rm buildConfig.xml"
}
