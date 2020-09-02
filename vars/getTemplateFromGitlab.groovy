import com.mulesoft.Constants

def call(String template) {
    deleteDir()
    def ssh_url = "git@github.com:M1053737/rest-template.git"
    
    git ssh_url: ssh_url // credentialsId: Constants.GITLAB_CREDENTIALS_ID,
    sh 'rm -r .git'
}
