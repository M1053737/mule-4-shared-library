import com.mulesoft.Constants

def call(String template) {
    deleteDir()
    def url = "git@github.com:M1053737/rest-template.git"
    ssh_url=url
    
    git url: url // credentialsId: Constants.GITLAB_CREDENTIALS_ID,
    sh 'rm -r .git'
}
