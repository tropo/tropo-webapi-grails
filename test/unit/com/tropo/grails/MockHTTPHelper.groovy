package com.tropo.grails

import net.sf.json.JSONObject

import static groovyx.net.http.Method.*

class MockHTTPHelper extends HTTPHelper {

    private launch_session_response_json = JSONObject.fromObject("{id='e81f20b21c9e2ee5fe874b9c578f2ab0', requestParams={token='72979191d971e344b46a0e4a3485571844250e689bb13548a75f1cce2ce9a53dde82c3fe944479bcb650500e'}, success='true', token='72979191d971e344b46a0e4a3485571844250e689bb13548a75f1cce2ce9a53dde82c3fe944479bcb650500e'}")
    private launch_session_response_json_multiple_params = JSONObject.fromObject("{id:'e81f20b21c9e2ee5fe874b9c578f2ab0', requestParams:{token='72979191d971e344b46a0e4a3485571844250e689bb13548a75f1cce2ce9a53dde82c3fe944479bcb650500e', customerName='JohnDyer', numberToDial='4075551212', msg='the sky is falling.'}, success='true', token='72979191d971e344b46a0e4a3485571844250e689bb13548a75f1cce2ce9a53dde82c3fe944479bcb650500e'}")
    private list_applications = [['id': '136290', 'href': 'https://api.tropo.com/v1/applications/136290', 'name': 'GrailsPluginTestsApp', 'platform': 'scripting', 'voiceUrl': 'http://www.website.com/index.json', 'messagingUrl': 'http://www.website2.com/index.json', 'partition': 'staging'], ['id': '136168', 'href': 'https://api.tropo.com/v1/applications/136168', 'name': 'GrailsTropoApp', 'platform': 'webapi', 'voiceUrl': 'http://77.27.59.2:8080/tropo-app/tropo/jordi', 'messagingUrl': 'http://77.27.59.2:8080/tropo-app/tropo/json', 'partition': 'staging'], ['id': '134827', 'href': 'https://api.tropo.com/v1/applications/134827', 'name': 'Placeo', 'platform': 'webapi', 'voiceUrl': 'http://77.27.59.2:8080/placeo/placeo/json', 'messagingUrl': 'http://77.27.59.2:8080/placeo/placeo/json', 'partition': 'staging'], ['id': '134167', 'href': 'https://api.tropo.com/v1/applications/134167', 'name': 'test', 'platform': 'scripting', 'voiceUrl': 'http://test.com/test.rb', 'messagingUrl': 'http://test.com/test.rb', 'partition': 'staging'], ['id': '134153', 'href': 'https://api.tropo.com/v1/applications/134153', 'name': 'TestMartinScripting', 'platform': 'scripting', 'voiceUrl': 'http://hosting.tropo.com/58863/www/testmartin.js', 'messagingUrl': 'http://hosting.tropo.com/58863/www/testmartin.js', 'partition': 'staging'], ['id': '134109', 'href': 'https://api.tropo.com/v1/applications/134109', 'name': 'TestMartin', 'platform': 'webapi', 'voiceUrl': 'http://77.27.59.2:8080/tropo-test/tropo/json', 'messagingUrl': 'http://77.27.59.2:8080/tropo-test/tropo/jsonsms', 'partition': 'staging']]
    private create_application = JSONObject.fromObject("{href:'https://api.tropo.com/v1/applications/136472', requestParams:{name='testapp516647780', voiceUrl='http://website.com', messagingUrl='http://website2.com', platform='scripting', partition:'staging'}, appId='136472'}")
    private delete = JSONObject.fromObject("{'message':'delete successful','requestParams':{}}")
    private add_number_prefix_1 = ['href': 'https://api.tropo.com/v1/applications/136290/addresses/number/+14076800712', 'requestParams': ['type': 'number', 'prefix': '1407'], 'appId': '136290', 'numberId': '+14076800712']
    private add_number_prefix_2 = ['href': 'https://api.tropo.com/v1/applications/136290/addresses/number/+34000111000', 'requestParams': ['type': 'number', 'prefix': '34'], 'appId': '136290', 'numberId': '+134000111000']
    private add_number_prefix_3 = ['href': 'https://api.tropo.com/v1/applications/136290/addresses/number/+1886000111000', 'requestParams': ['type': 'number', 'prefix': '34'], 'appId': '136290', 'numberId': '+1886000111000']
    private add_token = ['href': 'https://api.tropo.com/v1/applications/136290/addresses/token/949a4221be165645ba227de3b4d1a1d9cb41377b972f66595115a1832ee1491c9f326f8236d17e2e613be80e', 'requestParams': ['type': 'token', 'channel': 'voice'], 'appId': '136290', 'token': '949a4221be165645ba227de3b4d1a1d9cb41377b972f66595115a1832ee1491c9f326f8236d17e2e613be80e']
    private list_addresses = [['href': 'https://api.tropo.com/v1/applications/136290/addresses/sip/9992003056@sip.tropo.com', 'type': 'sip', 'address': '9992003056@sip.tropo.com'], ['href': 'https://api.tropo.com/v1/applications/136290/addresses/skype/+990009369992003056', 'type': 'skype', 'number': '+990009369992003056']]
    private list_exchanges = [['prefix': '1510', 'city': 'Richmond', 'state': 'CA', 'country': 'United States', 'description': 'Phone Number w/ SMS'], ['prefix': '1407', 'city': 'Orlando', 'state': 'FL', 'country': 'United States', 'description': 'Phone Number w/ SMS']]
    private send_signal = ['requestParams': ['value': 'exit'], 'status': 'QUEUED']
    private update_application = ['requestParams': ['name': 'GrailsPluginTestsApp', 'platform': 'webapi', 'partition': 'staging', 'appId': '136290'], 'name': 'GrailsPluginTestsApp', 'platform': 'webapi', 'partition': 'staging']
    private add_aim = ['requestParams': ['type': 'aim', 'username': 'tropocloud', 'password': 'password01', 'appId': '136290'], 'href': 'https://api.tropo.com/v1/applications/123456/addresses/aim/tropocloud']
    private add_number = ['requestParams': ['type': 'number', 'number': '4075551235'], 'href': 'https://api.tropo.com/v1/applications/addresses/number/+14075551235']

    public MockHTTPHelper(username, password) {

        super(username, password)
    }

    def Object doRequest(url, params, method) {

        println "Doing Fake Request : ${url} with params ${params} and method ${method}"
        if (url == 'https://api.tropo.com/v1/sessions?action=create') {
            if (params.customerName) {
                return launch_session_response_json_multiple_params
            } else {
                return launch_session_response_json
            }
        } else if (url == 'https://api.tropo.com/v1/applications') {
            if (method == POST) {
                return create_application
            } else if (method == GET) {
                return list_applications
            }
        } else if (url == 'https://api.tropo.com/v1/applications/136472') {
            if (method == DELETE) {
                return delete
            }
        } else if (url == 'https://api.tropo.com/v1/applications/136290') {
            if (method == PUT) {
                return update_application
            }
        } else if (url == 'https://api.tropo.com/v1/applications/136290/addresses') {
            if (params.prefix) {
                if (params.prefix == '1407') {
                    return add_number_prefix_1
                } else if (params.prefix == '34') {
                    return add_number_prefix_2
                } else if (params.prefix == '1886') {
                    return add_number_prefix_3
                }
            } else if (params.type == 'token') {
                return add_token
            } else if (params.type == 'aim') {
                return add_aim
            } else if (params.type == 'number') {
                return add_number
            } else if (url == 'https://api.tropo.com/v1/applications/136290/addresses') {
                return list_addresses
            }
        } else if (url == 'https://api.tropo.com/v1/applications/136290/addresses/number/+14076800712') {
            return delete
        } else if (url == 'https://api.tropo.com/v1/applications/136290/addresses/number/+134000111000') {
            return delete
        } else if (url == 'https://api.tropo.com/v1/applications/136290/addresses/number/+1886000111000') {
            return delete
        } else if (url == 'https://api.tropo.com/v1/exchanges') {
            return list_exchanges
        } else if (url == 'https://api.tropo.com/v1/sessions/e81f20b21c9e2ee5fe874b9c578f2ab0/signals') {
            return send_signal
        }
    }
}

