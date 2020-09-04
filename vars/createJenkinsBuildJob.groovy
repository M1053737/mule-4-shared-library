import com.mulesoft.Constants
import com.mulesoft.PipelinePlaceholders
import com.mulesoft.Secrets
import groovy.json.JsonSlurper



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
   
    
    //def folderName=pipelinePlaceholders.getEnvironment()
    echo "Jenkins"
    //echo folderName
    echo jobName
    echo Constants.JENKINS_DOMAIN

    def payload = readFile "buildConfig.xml"
    echo payload
    
    echo "******************KRISHNA START *************"
     
     def crumbResponse = httpRequest 'http://52.172.43.67:8080/crumbIssuer/api/json'
         println("Status: "+crumbResponse.status)
         println("Content: "+crumbResponse.content)
     def RawRecordsResponse = crumbResponse.content
     def json = new JsonSlurper().parseText(RawRecordsResponse)
     def crumbCode = json.'crumb'
        println("crumb Code: "+ crumbCode)
        
    
    
    def crumbResponseMap = new groovy.json.JsonSlurperClassic().parseText(crumbResponse.content)
      println("crumb Code MAP : "+ crumbResponseMap)
    echo "****************KRISHNA END**************"
   
    def response = httpRequest (
        httpMode: "POST",
        url: "http://${Constants.JENKINS_DOMAIN}/createItem?name=${jobName}",
        requestBody: payload,
        customHeaders: [[name: 'Authorization', value: "Basic ${authString}"], [name: 'Content-Type', value: 'application/xml'], [name: crumbResponseMap.crumbRequestField, value: crumbResponseMap.crumb]],
        quiet: true
        //validResponseCodes: '200:408'
     )
    
    sh "rm buildConfig.xml"
}
