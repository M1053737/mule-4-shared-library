import com.mulesoft.Constants
import com.mulesoft.PipelinePlaceholders

def fullRepoUrlWithCredentials
    
def call() {
    pipelinePlaceholders = PipelinePlaceholders.getInstance()

    sh "git init"
    sh "git remote add origin ${pipelinePlaceholders.getSshUrlToRepo()}"
    push("Initial commit", "master")

    // ---------------------------- NEEDS TO BE CHANGED TO SUPPORT BRANCHING ----------------------------

    // url = "${Constants.GITLAB_PROTOCOL}${Constants.GITLAB_DOMAIN}/api/v4/projects/${createRepoResponse.id}/repository/branches?branch=development&ref=master"
    // response = httpRequest (
    //     httpMode: "POST"
    //     , url: url
    //     , customHeaders: [[name: 'Private-Token', value: Constants.GITLAB_TOKEN]]
    // )

    // url = "${Constants.GITLAB_PROTOCOL}${Constants.GITLAB_DOMAIN}/api/v4/projects/${createRepoResponse.id}"
    // payload = '{ "default_branch": "development" }'
    // response = httpRequest (
    //     httpMode: "PUT"
    //     , url: url
    //     , requestBody: payload
    //     , authentication: Constants.GITLAB_CREDENTIALS_ID
    //     , customHeaders: [[name: 'Private-Token', value: Constants.GITLAB_TOKEN], [name: 'Content-Type', value: 'application/json']]
    // )

    // ---------------------------- NEEDS TO BE CHANGED TO SUPPORT BRANCHING ----------------------------
}

def push(commitMessage, branch) {
    pipelinePlaceholders = PipelinePlaceholders.getInstance()
    sh "git config --global user.email 'nagendra.ca2@mindtree.com'"
    sh "git config --global user.name 'M1053737'"
    sh "git remote set-url origin ${pipelinePlaceholders.getSshUrlToRepo()}"
    sh "git add ."
    sh "git commit -m '${commitMessage}'"
    sh "git push -u origin ${branch.toLowerCase()}"
}
