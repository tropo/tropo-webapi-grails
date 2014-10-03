package com.tropo.grails

import static org.junit.Assert.assertNotNull

class TropoServiceTests extends GroovyTestCase {

    private TESTING_APP_NAME = 'GrailsPluginTestsApp'
    private TESTING_APP_ID = '136290'
    private TESTING_APP_TOKEN = "72979191d971e344b46a0e4a3485571844250e689bb13548a75f1cce2ce9a53dde82c3fe944479bcb650500e"

    private TropoService service

    void setUp() {

        def username = System.getProperty("tropo.api.auth.username")
        def password = System.getProperty("tropo.api.auth.password")

        service = new TropoService(username, password)
        service.baseUrl = "https://api.tropo.com/v1/"

        // The tropo.api.prodtesting switch allows us to run this test suite agains production environment
        def p = System.getProperty("tropo.api.prodtesting")
        if (!(System.getProperty("tropo.api.prodtesting") == 'true')) {
            service.httpHelper = new MockHTTPHelper(username, password)
        }
    }

    void testLaunchSession() {

        def result = service.launchSession(token: TESTING_APP_TOKEN)
        assert result.success == "true"
    }

    void testFailsWithNoToken() {

        def message = shouldFail(TropoException) {
            service.launchSession()
        }
        assert message == "Missing parameter 'token'"
    }

    void testLaunchSessionMultipleParameters() {

        def result = service.launchSession(token: TESTING_APP_TOKEN, customerName: "JohnDyer", numberToDial: "4075551212", msg: "the sky is falling.")
        assert result.success == "true"
        assert result.requestParams['customerName'] == "JohnDyer"
    }

    void testListApplications() {

        def result = service.applications()
        assertNotNull result
        assert result.size() > 0
    }

    void testOurTestingApplicationExists() {

        // This testing application is used by several tests so it should exist

        def result = service.applications()
        def names = result.name
        assert names.contains(TESTING_APP_NAME)

    }

    void testCreateAndDeleteApplication() {

        def result = service.createApplication(name: "testapp" + new Random().nextInt(),
                voiceUrl: "http://website.com",
                messagingUrl: "http://website2.com",
                platform: "scripting",
                partition: "staging")

        assertNotNull result.href
        assertNotNull result.appId
        def appId = result.appId

        // Delete
        result = service.deleteApplication(appId: appId)

        println result
        assert result.message == 'delete successful'
    }

    void testNumbersBasedOnPrefix() {

        def result = service.addNumber(appId: TESTING_APP_ID, type: 'number', prefix: '1407')
        assertNotNull result
        assertNotNull result.numberId
        assert result.numberId.startsWith('+1407')

        def numberId = result.numberId

        result = service.remove(numberId: numberId, appId: TESTING_APP_ID)
        assert result.message == 'delete successful'
    }

    void testTollFreeNumbers() {

        // It needs billing account settings to work in production

        def result = service.addNumber(appId: TESTING_APP_ID, type: 'number', prefix: '1886')
        assertNotNull result
        assertNotNull result.numberId
        assert result.numberId.startsWith('+1886')

        def numberId = result.numberId

        result = service.remove(numberId: numberId, appId: TESTING_APP_ID)
        assert result.message == 'delete successful'
    }


    void testInternationalNumbers() {

        def result = service.addNumber(appId: TESTING_APP_ID, type: 'number', prefix: '34')
        assertNotNull result
        assertNotNull result.numberId
        assert result.numberId.startsWith('+134')

        def numberId = result.numberId

        result = service.remove(numberId: numberId, appId: TESTING_APP_ID)
        assert result.message == 'delete successful'
    }

    void testAddFailsWithNoAppId() {

        def message = shouldFail(TropoException) {
            service.addNumber(type: 'number', prefix: '34')()
        }
        assert message == "Missing parameter 'appId'"
    }

    void testAddFailsWithNoType() {

        def message = shouldFail(TropoException) {
            service.addNumber(appId: TESTING_APP_ID, prefix: '34')()
        }
        assert message == "Missing parameter 'type'"
    }

    void testAddFailsWithNoPrefix() {

        def message = shouldFail(TropoException) {
            service.addNumber(appId: TESTING_APP_ID, type: 'number')()
        }
        assert message == "Missing parameter 'prefix'"
    }

    void testRemoveFailsWithNoNumberId() {

        def message = shouldFail(TropoException) {
            service.remove(appId: TESTING_APP_ID)
        }
        assert message == "Missing parameter 'numberId'"
    }

    void testRemoveFailsWithNoAppId() {

        def message = shouldFail(TropoException) {
            service.remove(numberId: '1234')
        }
        assert message == "Missing parameter 'appId'"
    }

    void testMoveSpecificNumbers() {

        def result = service.move(appId: TESTING_APP_ID, type: 'number', number: '4075551235')
        assertNotNull result
        assertNotNull result.href
    }

    void testMoveFailsWithNoAppId() {

        def message = shouldFail(TropoException) {
            service.move(type: 'number', number: '4075551235')
        }
        assert message == "Missing parameter 'appId'"
    }

    void testMoveFailsWithNoNumber() {

        def message = shouldFail(TropoException) {
            service.move(appId: TESTING_APP_ID, type: 'number')
        }
        assert message == "Missing parameter 'number'"
    }

    void testMoveFailsWithNoType() {

        def message = shouldFail(TropoException) {
            service.move(appId: TESTING_APP_ID, number: '4075551235')
        }
        assert message == "Missing parameter 'type'"
    }

    void testAddIMAccounts() {

        def result = service.addIM(appId: TESTING_APP_ID, type: 'aim', username: 'tropocloud', password: 'password01')
        assertNotNull result
        assertNotNull result.href
    }

    void testAddToken() {

        def result = service.addToken(appId: TESTING_APP_ID, type: 'token', channel: 'voice')
        assertNotNull result
        assertNotNull result.token
    }

    void testUpdateApplication() {

        def result = service.updateApplication(appId: TESTING_APP_ID, name: TESTING_APP_NAME, partition: 'staging', platform: 'webapi')
        assertNotNull result
        assert result.partition == 'staging'
        assert result.platform == 'webapi'
    }

    void testListApplicationAddresses() {

        def result = service.addresses(appId: TESTING_APP_ID)
        assertNotNull result
        assert result.size() > 0
        assert result.channel.size() > 0
    }

    void testListAddressesFailsWithNoAppId() {

        def message = shouldFail(TropoException) {
            service.move(foo: 'bar')
        }
        assert message == "Missing parameter 'appId'"
    }

    void testListExchanges() {

        def result = service.exchanges()
        assertNotNull result
        assert result.size() > 0
        assert result.prefix.size() > 0
    }

    void testSendingSignal() {

        def result = service.launchSession(token: TESTING_APP_TOKEN)
        assert result.success == "true"
        def sessionId = result.id

        result = service.sendSignal(sessionId: sessionId, value: 'exit')
        assertNotNull result
        assert result.status == 'QUEUED' || 'NOTFOUND' // If its not quick enough we will get a NOTFOUND
    }

    void testSendingSignalFailsWithNoSessionId() {

        def message = shouldFail(TropoException) {
            service.sendSignal(value: 'exit')
        }
        assert message == "Missing parameter 'sessionId'"
    }

    void testSendingSignalFailsWithNoValue() {

        def message = shouldFail(TropoException) {
            service.sendSignal(sessionId: '111')
        }
        assert message == "Missing parameter 'value'"
    }
}
