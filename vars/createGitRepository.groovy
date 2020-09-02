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
            customHeaders: [[name: 'Authorization', value: "Token 47c673a32b2297050d5a93563b6c2663ca0fdeb9"], [name: 'Content-Type', value: 'application/json']],
            quiet: true,
            requestBody: repo_create_req,
            validResponseCodes: '200:404'
        )

    def responseMap = new groovy.json.JsonSlurperClassic().parseText(response.content)
    pipelinePlaceholders.setSshUrlToRepo(responseMap.ssh_url)
    pipelinePlaceholders.setHttpUrlToRepo(responseMap.url)
}
