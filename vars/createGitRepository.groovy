import com.mulesoft.Constants
import com.mulesoft.PipelinePlaceholders

def call() {
    pipelinePlaceholders = PipelinePlaceholders.getInstance()
    ORGANIZATION_FORMATTED = pipelinePlaceholders.getOrganizationFormatted()
    def url
    def data
    def api_name
    def repo_create_req
    if(pipelinePlaceholders.getApiAssetId()) {      // IMPLIES THIS IS AN API PROJECT 
        api_name = pipelinePlaceholders.getApiAssetIdFormatted()
        //echo api_name
        url = "https://api.github.com/user/repos"
        //data= '{"name": "api-test2" }'
        repo_create_req = """ { \"name\" : \"${api_name}\"} """
        
    }
    else {      // IMPLIES THIS IS A DOMAIN PROJECT
        url = "https://api.github.com/user/repos"
        repo_create_req = """ { \"name\" : \"${api_name}\"} """
    }

    echo url
    //echo data
    echo repo_create_req

    def response = httpRequest (
            httpMode: "POST",
            url: url,
           customHeaders: [[name: 'Authorization', value: "Basic bmFnZW5kcmEuY2EyQG1pbmR0cmVlLmNvbTpmZTUzM2Y3OGVlYTE0OWUwOTJjMjI0YTE1NzNiOGMyODg0NDhlNWNl"], [name: 'Content-Type', value: 'application/json']],
          // customHeaders: [[name: 'Authorization', value: "Token 9c192f5a69ee1f4510d638fa7049e9fbf7614fd6"], [name: 'Content-Type', value: 'application/json']],
           //quiet: true,
            requestBody: repo_create_req
            //validResponseCodes: '200:401'
        )

    def responseMap = new groovy.json.JsonSlurperClassic().parseText(response.content)
    //echo responseMap
    pipelinePlaceholders.setSshUrlToRepo(responseMap.ssh_url)
    pipelinePlaceholders.setHttpUrlToRepo(responseMap.clone_url)
}
