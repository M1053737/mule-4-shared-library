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

    def authString = "${secrets.getSecret('jenkins-api-token')}".getBytes().encodeBase64().toString()
    echo authString
    
    def s = "${secrets.getSecret('jenkins-api-token')}"
    echo "krishna"+s
    String encoded = s.bytes.encodeBase64().toString()
    echo encoded
    
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
      //def crumbResponse = httpRequest ( 
       //  httpMode: "GET",
        // url:'http://52.172.43.67:8080/crumbIssuer/api/json',
       
         // customHeaders: [[name: 'Authorization', value: "Basic YWRtaW46YWRtaW4xMjM="],[name: 'Content-Type', value: 'application/xml']]
           
          //)
           
        // println("Status: "+crumbResponse.status)
       // println("Content: "+crumbResponse.content)
       //  println("Content: "+crumbResponse.headers)
    
    //def crumbResponse1 = httpRequest ( 
        //  httpMode: "GET",
//url:'http://52.172.43.67:8080/crumbIssuer/api/json',
       
         // customHeaders: [[name: 'Authorization', value: "Basic YWRtaW46YWRtaW4xMjM="],[name: 'Content-Type', value: 'application/xml']]
          //)
           
         //println("Status: "+crumbResponse1.status)
       // println("Content: "+crumbResponse1.content)
        // println("Content: "+crumbResponse.headers)
    
   // def crumbResponseMap = new groovy.json.JsonSlurperClassic().parseText(crumbResponse.content)
   //   println("crumb Code MAP : "+ crumbResponseMap)
   //   echo crumbResponseMap.crumb
    echo "****************KRISHNA END**************"
  
    
    def payload = readFile "buildConfig.xml"
    echo payload
                    
    def response = httpRequest (
        httpMode: "POST",
        url: "http://52.172.43.67:8080/createItem?name=${jobName}",
      // url: "http://52.172.43.67:8080/job/${folderName}/createItem?name=${jobName}",
        requestBody: payload,
       //customHeaders: [[name: 'Authorization', value: "Basic  Basic YWRtaW46MTFhYWFkNDE2MjY0M2M4YzQ0NDQ2Y2Q4NjYxNTIxNzI2NQ=="],[name: 'Content-Type', value: 'application/xml'], [name: 'crumbRequestField', value: 'Jenkins-Crumb'],[name: 'crumb', value: 'crumbResponseMap.crumb']]
        customHeaders: [[name: 'Authorization', value: "Basic ${authString}"],[name: 'Content-Type', value: 'application/xml']]
        
       // customHeaders: [[name: 'Authorization', value: "Basic YWRtaW46YWRtaW4xMjM="],[name: 'Content-Type', value: 'application/xml'], [name: 'Jenkins-Crumb', value: "${crumbResponseMap.crumb}"],[name: 'JSESSIONID', value:"node0jt19d3bnepu2myb2ay1amq8051"]],
       // customHeaders: [[name: 'Authorization', value: "Token ${authString}"], [name: 'Content-Type', value: 'application/xml']],
    
        //quiet: true
        //validResponseCodes: '200:399,403'
     )
    
    sh "rm buildConfig.xml"
}
