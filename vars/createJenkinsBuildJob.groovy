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

    def authString = "root:${secrets.getSecret('jenkins-api-token')}".getBytes().encodeBase64().toString()
    //def authString = "root:${secrets.getSecret('jenkins-api-token')}"
    echo authString
     def folderName=pipelinePlaceholders.getEnvironment()
     echo folderName
   
    
    //def folderName=pipelinePlaceholders.getEnvironment()
    echo "Jenkins"
    //echo folderName
    echo jobName
    echo Constants.JENKINS_DOMAIN

    
    
    echo "******************KRISHNA START *************"
     
 //   def crumbResponse = httpRequest 'http://52.172.43.67:8080/crumbIssuer/api/json'
       // println("Status: "+crumbResponse.status)
       //  println("Content: "+crumbResponse.content)
     //def RawRecordsResponse = crumbResponse.content
     //def json = new JsonSlurper().parseText(RawRecordsResponse)
     //def crumbCode = json.'crumb'
       // println("crumb Code: "+ crumbCode)
      def crumbResponse = httpRequest ( 
          httpMode: "GET",
         url:'http://52.172.43.67:8080/crumbIssuer/api/json',
       
          customHeaders: [[name: 'Authorization', value: "Basic YWRtaW46YWRtaW4xMjM="],[name: 'Content-Type', value: 'application/xml']],
            " Cache-Control" : no-cache
          )
           
         println("Status: "+crumbResponse.status)
        println("Content: "+crumbResponse.content)
    
    def crumbResponse1 = httpRequest ( 
          httpMode: "GET",
         url:'http://52.172.43.67:8080/crumbIssuer/api/json',
        Cache-Control : no-cache,
          customHeaders: [[name: 'Authorization', value: "Basic YWRtaW46YWRtaW4xMjM="],[name: 'Content-Type', value: 'application/xml']]
          )
           
         println("Status: "+crumbResponse1.status)
        println("Content: "+crumbResponse1.content)
        
    def crumbResponseMap = new groovy.json.JsonSlurperClassic().parseText(crumbResponse.content)
      println("crumb Code MAP : "+ crumbResponseMap)
      echo crumbResponseMap.crumb
    echo "****************KRISHNA END**************"
  
    
    def payload = readFile "buildConfig.xml"
    echo payload
                    
    def response = httpRequest (
        httpMode: "POST",
        url: "http://52.172.43.67:8080/createItem?name=${jobName}",
       // url: "http://52.172.43.67:8080/job/${folderName}/createItem?name=${jobName}",
        requestBody: payload,
       // customHeaders: [[name: 'Authorization', value: "Basic YWRtaW46YWRtaW4xMjM="],[name: 'Content-Type', value: 'application/xml'], [name: 'crumbRequestField', value: 'Jenkins-Crumb'],[name: 'crumb', value: 'crumbResponseMap.crumb']]
        customHeaders: [[name: 'Authorization', value: "Basic YWRtaW46YWRtaW4xMjM="],[name: 'Content-Type', value: 'application/xml'], [name: 'Jenkins-Crumb', value: "${crumbResponseMap.crumb}"]],
        Cache-Control : no-cache
        //quiet: true
        //validResponseCodes: '200:403'
     )
    
    sh "rm buildConfig.xml"
}
