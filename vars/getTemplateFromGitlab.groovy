import com.mulesoft.Constants
import com.mulesoft.PipelinePlaceholders

 def call() {
    pipelinePlaceholders = PipelinePlaceholders.getInstance()
 }

def call(String template) {
    deleteDir()
   
    //def url = "git@github.com:M1053737/rest-template.git"
   //def url = "git@github.com:M1053737/rest-template-master1.git" 
    def url =  pipelinePlaceholders.getRestTempUrl() 
   
    //def url = "https://github.com/M1053737/rest-template.git"
   
    
   
    git url: url // credentialsId: Constants.GITLAB_CREDENTIALS_ID,
    sh 'rm -r .git'
}
