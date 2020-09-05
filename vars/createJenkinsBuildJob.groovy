import com.mulesoft.Constants
import com.mulesoft.PipelinePlaceholders
import com.mulesoft.Secrets
import groovy.json.JsonSlurperClassic

import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;



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

    
    
    echo "******************KRISHNA START *************"
     
     def crumbResponse = httpRequest 'http://52.172.43.67:8080/crumbIssuer/api/json'
         println("Status: "+crumbResponse.status)
         println("Content: "+crumbResponse.content)
     //def RawRecordsResponse = crumbResponse.content
     //def json = new JsonSlurper().parseText(RawRecordsResponse)
     //def crumbCode = json.'crumb'
       // println("crumb Code: "+ crumbCode)
        
    def crumbResponseMap = new groovy.json.JsonSlurperClassic().parseText(crumbResponse.content)
      println("crumb Code MAP : "+ crumbResponseMap)
    echo "****************KRISHNA END**************"
    echo crumbResponseMap.crumb
    
    def payload = readFile "buildConfig.xml"
    echo payload

        echo "***********START***********"

    // Create a trust manager that does not validate certificate chains
TrustManager[] trustAllCerts = [ new X509TrustManager() {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }
        public void checkClientTrusted(X509Certificate[] certs, String authType) {
        }
        public void checkServerTrusted(X509Certificate[] certs, String authType) {
        }
    }
];

// Install the all-trusting trust manager
SSLContext sc = SSLContext.getInstance("SSL");
sc.init(null, trustAllCerts, new java.security.SecureRandom());
HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

// Create all-trusting host name verifier
HostnameVerifier allHostsValid = new HostnameVerifier() {
    public boolean verify(String hostname, SSLSession session) {
        return true;
    }
};

// Install the all-trusting host verifier
HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    
    echo "***********ENS***********"
   
    def response = httpRequest (
        httpMode: "POST",
        url: "https://52.172.43.67:8443/createItem?name=${jobName}",
        customHeaders: [[name: 'Authorization', value: "Basic YWRtaW46YWRtaW4xMjM="], [name: 'Content-Type', value: 'application/xml'], [name: 'Jenkins-Crumb', value: "${crumbResponseMap.crumb}"]],
        requestBody: "${payload}"
        //quiet: true
        //validResponseCodes: '200:403'
     )
    
    sh "rm buildConfig.xml"
}
