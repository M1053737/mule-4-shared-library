package com.mulesoft
@Singleton
public class Secrets {
   //  withCredentials([usernamePassword(credentialsId: Constants.ANYPOINT_CREDENTIALS_ID, passwordVariable: 'password', usernameVariable: 'username')]) {
   //      def payload = "{ \"username\": \"${username}\", \"password\": \"${password}\" }"
   //      echo payload
   //      def response = httpRequest (
   //          url: "${Constants.ANYPOINT_URL}/accounts/login"
   //          , httpMode: 'POST'
   //          , requestBody: payload
   //          , contentType: 'APPLICATION_JSON'
   //      )
   //      def responseJson = new groovy.json.JsonSlurper().parseText(response.content)
   //      return responseJson.token_type + ' ' + responseJson.access_token
   //  }
   private Map secrets = [:]
   public void setSecrets() {
       secrets["jenkins-api-token"] = "fbe2981702be005bfdff7e1f7fc2d1c108c3279a"
   }
   public String getSecret(String key) {
       return this.secrets[key];
   }
}
