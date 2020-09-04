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
    echo payload
    def url =  "http://${Constants.JENKINS_DOMAIN}/createItem?name=${jobName}"
    echo url
    
    def urlget= "http://52.172.43.67:8080/crumbIssuer/api/json"
    echo urlget
    
    def request = httpRequest (
        httpMode: "GET",
        url:urlget ,
        customHeaders: [[name: 'Authorization', value: "Basic YWRtaW46YWRtaW4xMjM="], [name: 'Content-Type', value: 'application/xml']],
    println($request.content)       
    )
     
    echo request
    
   
    def response = httpRequest (
        httpMode: "POST",
        url:url,
       //url: "http://${Constants.JENKINS_DOMAIN}/createItem?name=${jobName}",
      //  url: "http://52.172.43.67:8080/createItem?name=NEWJOB4534222321",
        //url: "http://${Constants.JENKINS_DOMAIN}/job/${folderName}/createItem?name=${jobName}",
        
        //customHeaders: [[name: 'Authorization', value: "Token ${authString}"], [name: 'Content-Type', value: 'application/xml']],
        customHeaders: [[name: 'Authorization', value: "Basic YWRtaW46YWRtaW4xMjM="], [name: 'Content-Type', value: 'application/xml'], [name: 'Jenkins-Crumb', value: "55f092c2df3ecf5d682d8e6d74b2f8c2faea389f9c143755eebba4d61ee19552"]],
        //customHeaders: [[name: 'Authorization', value: "Token ${authString}"], [name: 'Content-Type', value: 'application/xml'], [name: 'Jenkins-Crumb', value: "55f092c2df3ecf5d682d8e6d74b2f8c2faea389f9c143755eebba4d61ee19552"]],
        quiet: true,
        requestBody: payload
        //validResponseCodes: '200:408'
   
    )
    
    sh "rm buildConfig.xml"
}
