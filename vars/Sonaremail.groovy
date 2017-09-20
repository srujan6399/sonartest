#!/usr/bin/env groovy

 def jsonParse(def json) {
 new groovy.json.JsonSlurperClassic().parseText(json)
}
def call(body) {

  def config = [:]
  body.resolveStrategy = Closure.DELEGATE_FIRST
  body.delegate = config
  body()
  
  
    def response = httpRequest ignoreSslErrors:true, authentication: 'sonar',  url: 'http://localhost:9000/api/issues/search'
      def re= jsonParse(response.content);   
      println(re.issues);
      
      String rows="";
      String header="";
      for (int i=4; i<re.issues.size(); i++) {
          header=header+"<tr><td>"+re.issues[i].component+"</td></tr>"
      }
      println(header);
      for (int i=0; i<re.issues.size(); i++) {
          rows=rows+"<tr><td>"+re.issues[i].textRange.startLine+"</td><td>"+re.issues[i].key+"</td></tr>"
      }
      
      writeFile file: 'report.html', text: '<html><body><table border="3">'+rows+'</table></body></html>'


 
 }
