package com.tropo.grails

import grails.util.Holders


/**
 * This Grails service abstracts the access to the <a href="https://www.tropo.com/docs/rest/rest_api.htm">Tropo REST API</a>.</p> 
 *  
 * @author martin
 *
 */
class TropoService {

    static transactional = true
	def baseUrl
	def httpHelper
	def paramsHelper
	
	private String username
	private String password
	
	private String HARDCODED_BASE_URL = "https://api.tropo.com/1.0/"

	public TropoService(String username, String password) {
		
		this.username = username
		this.password = password

        baseUrl = Holders.config?.tropo?.api?.url  //ConfigurationHolder?.config?.tropo?.api?.url
		if (!baseUrl) {
			baseUrl = HARDCODED_BASE_URL
		}
		
		if (!username) {
			// try to find it on grails config file
			username = Holders.config?.tropo?.username    //ConfigurationHolder?.config?.tropo?.username
		}
		if (!password) {
			// try to find it on grails config file
			username = Holders.config?.tropo?.password    //ConfigurationHolder?.config?.tropo?.password
		}

		httpHelper = new HTTPHelper(username,password)
		paramsHelper = new ParamsHelper()

	}
	
	public TropoService() {

		this(null,null)		
	}
	
    def launchSession(Map params = [:]) {

		def url = "${baseUrl}sessions?action=create" as String
		paramsHelper.checkParams(['token'],params)
		return httpHelper.doPost(url, params)
    }
		
	def createApplication(Map params = [:]) {
		
		def url = "${baseUrl}applications" as String
		paramsHelper.checkParams(['name','voiceUrl','messagingUrl','platform','partition'],params)
		return httpHelper.doPost(url, params)
	}
	
	def updateApplication(Map params = [:]) {
		
		paramsHelper.checkParams(['appId'],params)
		def url = "${baseUrl}applications/${paramsHelper.getAndRemove('appId',params)}" as String
		return httpHelper.doPut(url, params)
	}
	
	def deleteApplication(Map params = [:]) {
		
		paramsHelper.checkParams(['appId'],params)
		def url = "${baseUrl}applications/${paramsHelper.getAndRemove('appId',params)}" as String
		return httpHelper.doDelete(url, params)
	}
	
	def applications() {
		
		def url = "${baseUrl}applications" as String
		return httpHelper.doGet(url, [:])
	}
	
	def exchanges() {
		
		def url = "${baseUrl}exchanges" as String
		return httpHelper.doGet(url, [:])
	}
	
	def addresses(Map params = [:]) {
		
		paramsHelper.checkParams(['appId'],params)
		def url = "${baseUrl}applications/${paramsHelper.getAndRemove('appId',params)}/addresses" as String
		return httpHelper.doGet(url, [:])
	}
	
	def addNumber(Map params = [:]) {
		
		paramsHelper.checkParams(['appId','type','prefix'],params)
		def url = "${baseUrl}applications/${paramsHelper.getAndRemove('appId',params)}/addresses" as String
		return httpHelper.doPost(url, params)
	}
	
	def addIM(Map params = [:]) {
		
		paramsHelper.checkParams(['appId','type','username','password'],params)
		def url = "${baseUrl}applications/${paramsHelper.getAndRemove('appId',params)}/addresses" as String
		return httpHelper.doPost(url, params)
	}
	
	def addToken(Map params = [:]) {
		
		paramsHelper.checkParams(['appId','type','channel'],params)
		def url = "${baseUrl}applications/${paramsHelper.getAndRemove('appId',params)}/addresses" as String
		return httpHelper.doPost(url, params)
	}
	
	def move(Map params = [:]) {
		
		paramsHelper.checkParams(['appId','type','number'],params)
		def url = "${baseUrl}applications/${paramsHelper.getAndRemove('appId',params)}/addresses" as String
		return httpHelper.doPost(url, params)
	}

	def remove(Map params = [:]) {
		
		paramsHelper.checkParams(['appId','numberId'],params)
		def url = "${baseUrl}applications/${paramsHelper.getAndRemove('appId',params)}/addresses/number/${paramsHelper.getAndRemove('numberId',params)}" as String
		
		return httpHelper.doDelete(url, params)
	}

	def sendSignal(Map params = [:]) {

		paramsHelper.checkParams(['sessionId','value'],params)
		def url = "${baseUrl}sessions/${paramsHelper.getAndRemove('sessionId',params)}/signals" as String
		return httpHelper.doPost(url, params)
	}
	

}
