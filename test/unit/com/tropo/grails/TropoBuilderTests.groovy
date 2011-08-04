package com.tropo.grails

import groovy.util.GroovyTestCase;

import java.util.Map


class TropoBuilderTests extends GroovyTestCase {
	
	public void testAskAction() {
		
		def builder = new TropoBuilder()
		builder.tropo {
			ask(name : 'foo', bargein: true, timeout: 30, required: true)
		}
		assert builder.text() == "{\"tropo\":[{\"ask\":{\"name\":\"foo\",\"bargein\":true,\"timeout\":30,\"required\":true}}]}"
	}
	
	public void testAskWithSayBlock() {

		def builder = new TropoBuilder()
		builder.tropo {
			ask(name : 'foo', bargein: true, timeout: 30, required: true, choices: '[5 DIGITS]') {
				say('Please say your account number')
			}
		}
		assert builder.text() == "{\"tropo\":[{\"ask\":{\"name\":\"foo\",\"bargein\":true,\"timeout\":30,\"required\":true,\"choices\":[\"5 DIGITS\"],\"say\":[{\"value\":\"Please say your account number\"}]}}]}"

	}
	
	public void testAskWithSayAndOnBlocks() {

		def builder = new TropoBuilder()
		builder.tropo {
			ask(name : 'foo', bargein: true, timeout: 30, required: true) {
				say('Please say your account number')				
				choices(value: '[5 DIGITS]')
			}
			on(event:'success',next:'/result.json')
		}
		assert builder.text() == "{\"tropo\":[{\"ask\":{\"name\":\"foo\",\"bargein\":true,\"timeout\":30,\"required\":true,\"say\":[{\"value\":\"Please say your account number\"}],\"choices\":{\"value\":\"[5 DIGITS]\"}}},{\"on\":{\"event\":\"success\",\"next\":\"/result.json\"}}]}"
	}  
		
	public void testFailsAskWithNoNameParameter() {
		
		def builder = new TropoBuilder()
		def message = shouldFail(TropoBuilderException) {
			builder.tropo {
				ask(foo:'bar')
			}
		}
		assert message == "Missing required property: 'name'"
	}
	
	public void testChoice() {

		def builder = new TropoBuilder()
		builder.tropo {
			choices(value: '[5 DIGITS]')
		}
		assert builder.text() == "{\"tropo\":[{\"choices\":{\"value\":\"[5 DIGITS]\"}}]}"
	}
	
	public void testFailsChoicesWithUnsupportedMode() {
		
		def builder = new TropoBuilder()
		def message = shouldFail(TropoBuilderException) {
			builder.tropo {
				choices(value:'[5 DIGITS]', mode:'frootloops')
			}
		}
		assert message == "If mode is provided, only 'dtmf', 'speech' or 'any' is supported"
	}
	
	public void testChoiceWithMode() {
		
		def builder = new TropoBuilder()
		builder.tropo {
			choices(value: '[5 DIGITS]', mode: 'dtmf')
		}
		assert builder.text() == "{\"tropo\":[{\"choices\":{\"value\":\"[5 DIGITS]\",\"mode\":\"dtmf\"}}]}"
	}
	
	public void testConference() {
		
		def builder = new TropoBuilder()
		builder.tropo {
			conference(name : 'foo', id: '1234', mute: false, send_tones: false, exit_tone: '#')
		}
		assert builder.text() == "{\"tropo\":[{\"conference\":{\"name\":\"foo\",\"id\":\"1234\",\"mute\":false,\"send_tones\":false,\"exit_tone\":\"#\"}}]}"
	}

	public void testConferenceWithOnAndSayBlocks() {

		def builder = new TropoBuilder()
		builder.tropo {
			conference(name: 'foo', id: '1234', mute: false, send_tones: false, exit_tone: '#') {
				on(event: 'join') {
					say(value: 'Welcome to the conference')
				}
				on(event:'leave') {
					say(value: 'Someone has left the conference')
				}
			}
		}
		assert builder.text() == "{\"tropo\":[{\"conference\":{\"name\":\"foo\",\"id\":\"1234\",\"mute\":false,\"send_tones\":false,\"exit_tone\":\"#\",\"on\":[{\"event\":\"join\",\"say\":[{\"value\":\"Welcome to the conference\"}]},{\"event\":\"leave\",\"say\":[{\"value\":\"Someone has left the conference\"}]}]}}]}"
	}
		
	public void testFailsConferenceWithNoNameParameter() {
		
		def builder = new TropoBuilder()
		def message = shouldFail(TropoBuilderException) {
			builder.tropo {
				conference(foo:'bar')
			}
		}
		assert message == "Missing required property: 'name'"
	}
	
	public void testFailsConferenceWithNoIdParameter() {
		
		def builder = new TropoBuilder()
		def message = shouldFail(TropoBuilderException) {
			builder.tropo {
				conference(name:'bar')
			}
		}
		assert message == "Missing required property: 'id'"
	}
	
	public void testHangup() {
		
		def builder = new TropoBuilder()
		builder.tropo {
			hangup()
		}
		assert builder.text() == "{\"tropo\":[{\"hangup\":null}]}"
	}
	
	public void testOn() {

		def builder = new TropoBuilder()
		builder.tropo {
			on(event: 'hangup', next: 'myresource')
		}
		assert builder.text() == "{\"tropo\":[{\"on\":{\"event\":\"hangup\",\"next\":\"myresource\"}}]}"
	}
	
	public void testFailsOnWithNoEventParameter() {
		
		def builder = new TropoBuilder()
		def message = shouldFail(TropoBuilderException) {
			builder.tropo {
				on(foo: 'bar', next: 'myresource')
			}
		}
		assert message == "Missing required property: 'event'"
	}
	
	public void testRecord() {
	
		def builder = new TropoBuilder()
		builder.tropo {
			record(name: 'foo', url: 'http://sendme.com/tropo', beep: true, send_tones: false, exit_tone: '#')
		}
		assert builder.text() == "{\"tropo\":[{\"record\":{\"name\":\"foo\",\"url\":\"http://sendme.com/tropo\",\"beep\":true,\"send_tones\":false,\"exit_tone\":\"#\"}}]}"		
	}
	
	public void testFailsRecordWithNoNameParameter() {
		
		def builder = new TropoBuilder()
		def message = shouldFail(TropoBuilderException) {
			builder.tropo {
				record(foo:'bar')
			}
		}
		assert message == "Missing required property: 'name'"
	}
	
	public void testFailsRecordWithNoUrlParameter() {
		
		def builder = new TropoBuilder()
		def message = shouldFail(TropoBuilderException) {
			builder.tropo {
				record(name:'bar')
			}
		}
		assert message == "Missing required property: 'url'"
	}
	
	public void testFailsRecordWithInvalidUrl() {
		
		def builder = new TropoBuilder()
		def message = shouldFail(TropoBuilderException) {
			builder.tropo {
				record(name: 'foo', url: 'invalid' )
			}
		}
		assert message == "The 'url' parameter must be a valid URL"
	}
	
	public void testRecordAcceptsEmailAddress() {
		
		def builder = new TropoBuilder()
		builder.tropo {
			record(name: 'foo', url: 'mailto:foo@bar.com', beep: true, send_tones: false, exit_tone: '#')
		}
		assert builder.text() == "{\"tropo\":[{\"record\":{\"name\":\"foo\",\"url\":\"foo@bar.com\",\"beep\":true,\"send_tones\":false,\"exit_tone\":\"#\"}}]}"		
	}
	
	public void testRedirect() {
		
		def builder = new TropoBuilder()
		builder.tropo {
			redirect(to: 'sip:1234', from: '4155551212')
		}
		assert builder.text() == "{\"tropo\":[{\"redirect\":{\"to\":\"sip:1234\",\"from\":\"4155551212\"}}]}"
	}
	
	public void testFailsNestedRedirect() {
		
		def builder = new TropoBuilder()
		def message = shouldFail(TropoBuilderException) {
			builder.tropo {
				conference(name: 'foo', id: '2334' ) {
					redirect(to: 'sip:1234', from: '4155551212')
				}
			}
		}
		assert message == "Redirect should only be used alone and before the session is answered, use transfer instead"
	}
	
	public void testFailsRedirectWithNoTo() {
		
		def builder = new TropoBuilder()
		def message = shouldFail(TropoBuilderException) {
			builder.tropo {
				redirect(from: '4155551212')
			}
		}
		assert message == "Missing required property: 'to'"
	}
	
	public void testReject() {
		
		def builder = new TropoBuilder()
		builder.tropo {
			reject()
		}
		assert builder.text() == "{\"tropo\":[{\"reject\":null}]}"

	}
	
	public void testFailsOnEmptyBuilder() {
	
		def message = shouldFail(TropoBuilderException) {
			new TropoBuilder().text()
		}
		assert message == "You need a tropo root node!"
	}
	
	public void testTropoRoot() {
		
		def builder = new TropoBuilder()
		builder.tropo {}
		assert builder.text() == "{\"tropo\":[]}"
	}
	
	public void testSay() {
		
		def builder = new TropoBuilder()
		
		builder.tropo {
			say('1234')
		}
		assert builder.text() == "{\"tropo\":[{\"say\":[{\"value\":\"1234\"}]}]}"
	}
	
	public void testSayWithNumbersAndDobleQuotes() {
	
		def builder = new TropoBuilder()
		
		builder.tropo {
			say("Your zipcode is ${14000}. oh yes.")
		}
		assert builder.text() == "{\"tropo\":[{\"say\":[{\"value\":\"Your zipcode is 14000. oh yes.\"}]}]}"
	}
	
	public void testSayWithMapArgument() {
		
		def builder = new TropoBuilder()
		
		builder.tropo {
			say(value:'1234')
		}
		assert builder.text() == "{\"tropo\":[{\"say\":[{\"value\":\"1234\"}]}]}"
	}
	
	public void testFailsWithNumbers() {
		
		def builder = new TropoBuilder()
		
		def message = shouldFail(TropoBuilderException) {
			builder.tropo {
				say(1234)
			}
		}
		assert message == "An invalid paramater type class java.lang.Integer has been passed"
	}
	
	public void testSayWithArrayArgument() {
		
		def builder = new TropoBuilder()
		builder.tropo {
			say([value: '1234'], [value: 'abcd', event: 'nomatch:1'])
		}
		
		assert builder.text() == "{\"tropo\":[{\"say\":[{\"value\":\"1234\"},{\"value\":\"abcd\",\"event\":\"nomatch:1\"}]}]}"
	}

	public void testSayWithMoreThanTwoArrayArguments() {
		
		def builder = new TropoBuilder()
		
		builder.tropo {
			say([[value: '1234'], [value: 'abcd', event: 'nomatch:1'], [value: 'zywx', event: 'nomatch:2']])
		}
		assert builder.text() == "{\"tropo\":[{\"say\":[{\"value\":\"1234\"},{\"value\":\"abcd\",\"event\":\"nomatch:1\"},{\"value\":\"zywx\",\"event\":\"nomatch:2\"}]}]}"
	}

	public void testFailsWithNoValuePassed() {
		
		def builder = new TropoBuilder()
		
		def message = shouldFail(TropoBuilderException) {
			builder.tropo {
				say(name:'blah')
			}
		}
		assert message == "Missing required property: 'value'"
	}
	
	public void testSayAndOn() {
		
		TropoBuilder builder = new TropoBuilder()
		builder.tropo {
			say(value:'blah')
			on(event: 'error', next: 'error.json')
			
		}
		assert builder.text() == "{\"tropo\":[{\"say\":[{\"value\":\"blah\"}]},{\"on\":{\"event\":\"error\",\"next\":\"error.json\"}}]}"
	}
	
	public void testStartRecording() {
		
		TropoBuilder builder = new TropoBuilder()
		builder.tropo {
			startRecording(url:'http://postrecording.com/tropo')
		}
		assert builder.text() == "{\"tropo\":[{\"startRecording\":{\"url\":\"http://postrecording.com/tropo\"}}]}"
	}

	public void testStopRecording() {
		
		def builder = new TropoBuilder()
		builder.tropo {
			stopRecording()
		}
		assert builder.text() == "{\"tropo\":[{\"stopRecording\":null}]}"

	}

	public void testTransfer() {
		
		def builder = new TropoBuilder()
		builder.tropo {
			transfer(to: 'tel:+14157044517')
		}
		assert builder.text() == "{\"tropo\":[{\"transfer\":{\"to\":\"tel:+14157044517\"}}]}"
	}
	
	public void testTransferWithOnAndChoices() {
		
		def builder = new TropoBuilder()
		builder.tropo {
			transfer(to: 'tel:+14157044517') {
				on(event: 'unbounded', next: '/error.json')
				choices(value: '[5 DIGITS]')
			}
		}
		assert builder.text() == "{\"tropo\":[{\"transfer\":{\"to\":\"tel:+14157044517\",\"on\":[{\"event\":\"unbounded\",\"next\":\"/error.json\"}],\"choices\":{\"value\":\"[5 DIGITS]\"}}}]}"
	}
	
	public void testFailsTransferWithNoToParameter() {
		
		def builder = new TropoBuilder()
		
		def message = shouldFail(TropoBuilderException) {
			builder.tropo {
				transfer()
			}
		}
		assert message == "Missing required property: 'to'"
	}

	public void testGenerateJsonWhenBlockIsPassed() {
		
		def builder = new TropoBuilder()
		builder.tropo {
			say([value: '1234'], [value: 'abcd', event: 'nomatch:1'])
			say([value: '0987'], [value: 'zyxw', event: 'nomatch:2'])
		}
		assert builder.text() == "{\"tropo\":[{\"say\":[{\"value\":\"1234\"},{\"value\":\"abcd\",\"event\":\"nomatch:1\"}]},{\"say\":[{\"value\":\"0987\"},{\"value\":\"zyxw\",\"event\":\"nomatch:2\"}]}]}"		
	}

	public void testParseJsonSession() {

		def builder = new TropoBuilder()
		def json_session = "{\"session\":{\"id\":\"dih06n\",\"accountId\":\"33932\",\"timestamp\":\"2010-01-19T23:18:48.562Z\",\"userType\":\"HUMAN\",\"to\":{\"id\":\"tropomessaging@bot.im\",\"name\":\"unknown\",\"channel\":\"TEXT\",\"network\":\"JABBER\"},\"from\":{\"id\":\"john_doe@gmail.com\",\"name\":\"unknown\",\"channel\":\"TEXT\",\"network\":\"JABBER\"}}}"
		def map = builder.parse(json_session)
		def t = map.session.timestamp
		assert map.session.timestamp == '2010-01-19T23:18:48.562Z'
	}
	
	public void testShouldSeeObjectOutsideBlock() {
		
		def builder = new TropoBuilder()
		def t = 'foobar'
		def newt
		builder.tropo {
			newt = t
			say(value:'blah')
			on(event:'error', next:'error.json')
		}
		assert newt == 'foobar'
	}

	public void testTwoSaysInBlock() {
		
		def builder = new TropoBuilder()
		builder.tropo {
			say('foo')
			say('bar')
		}
		assert builder.text() == "{\"tropo\":[{\"say\":[{\"value\":\"foo\"}]},{\"say\":[{\"value\":\"bar\"}]}]}"
	}

	public void testTwoSaysSeparated() {
		
		def builder = new TropoBuilder()
		builder.say('foo')
		builder.say('bar')

		assert builder.text() == "{\"tropo\":[{\"say\":[{\"value\":\"foo\"}]},{\"say\":[{\"value\":\"bar\"}]}]}"
	}

	public void testSayOnAndRecordSeparated() {

		def builder = new TropoBuilder()
		builder.say('Welcome to the app')
		builder.on(event: 'hangup', next: '/hangup.json')
		builder.record(name: 'foo', beep: true, send_tones: false, exit_tone: '#', url: 'http://sendme.com/tropo') {
			say(value: 'Please say your account number')
			choices(value: '[5 DIGITS]')
		}
		
		assert builder.text() == "{\"tropo\":[{\"say\":[{\"value\":\"Welcome to the app\"}]},{\"on\":{\"event\":\"hangup\",\"next\":\"/hangup.json\"}},{\"record\":{\"name\":\"foo\",\"beep\":true,\"send_tones\":false,\"exit_tone\":\"#\",\"url\":\"http://sendme.com/tropo\",\"say\":[{\"value\":\"Please say your account number\"}],\"choices\":{\"value\":\"[5 DIGITS]\"}}}]}"
	}
	
	public void testReset() {
		
		def builder = new TropoBuilder()
		builder.ask(name : 'foo', bargein: true, timeout: 30, required: true)		
		assert builder.text() == "{\"tropo\":[{\"ask\":{\"name\":\"foo\",\"bargein\":true,\"timeout\":30,\"required\":true}}]}"
		
		builder.reset()
		assert builder.text() == "{\"tropo\":[]}"
	}
	
	public void testResultWithActionIsParsed() {
		
		def builder = new TropoBuilder()
		def json_result = "{\"result\":{\"sessionId\":\"CCFD9C86-1DD1-11B2-B76D-B9B253E4B7FB@161.253.55.20\",\"callState\":\"ANSWERED\",\"sessionDuration\":2,\"sequence\":1,\"complete\":true,\"error\":null,\"actions\":{\"name\":\"zip\",\"attempts\":1,\"disposition\":\"SUCCESS\",\"confidence\":100,\"interpretation\":\"12345\",\"utterance\":\"1 2 3 4 5\"}}}"
		def map = builder.parse(json_result)
		
		assert map.result.actions.name == 'zip'
	}
	
	public void testObjectArrivesInJson() {

		def builder = new TropoBuilder()
		def json_result = "{\"result\":{\"sessionId\":\"CCFD9C86-1DD1-11B2-B76D-B9B253E4B7FB@161.253.55.20\",\"callState\":\"ANSWERED\",\"sessionDuration\":2,\"sequence\":1,\"complete\":true,\"error\":null,\"actions\":{\"name\":\"zip\",\"attempts\":1,\"disposition\":\"SUCCESS\",\"confidence\":100,\"interpretation\":\"12345\",\"utterance\":\"1 2 3 4 5\"}}}"
		def map = builder.parse(json_result)
		
		def a = map.result
		assert map['result']['callState'] == 'ANSWERED'
		assert map.result.callState == 'ANSWERED'
	}
	
	public void testStartRecordingLargeScript() {
		
		def builder = new TropoBuilder()
		
		builder.on(event: 'error', next:'/error.json') // For fatal programming errors. Log some details so we can fix it
		builder.on(event: 'hangup', next:'/hangup.json') // When a user hangs or call is done. We will want to log some details.
		builder.on(event: 'continue', next:'/next.json')
		builder.say('Hello')
		builder.startRecording(url: 'http://heroku-voip.marksilver.net/post_audio_to_s3?filename=foo.wav&unique_id=bar')
		// [From this point, until stop_recording(), we will record what the caller *and* the IVR say]
		builder.say 'You are now on the record.'
		// Prompt the user to incriminate themselve on-the-record
		builder.say 'Go ahead, sing-along.'
		builder.say "http://denalidomain.com/music/keepers/HappyHappyBirthdaytoYou-Disney.mp3"
	 
		assert builder.text() == "{\"tropo\":[{\"on\":{\"event\":\"error\",\"next\":\"/error.json\"}},{\"on\":{\"event\":\"hangup\",\"next\":\"/hangup.json\"}},{\"on\":{\"event\":\"continue\",\"next\":\"/next.json\"}},{\"say\":[{\"value\":\"Hello\"}]},{\"startRecording\":{\"url\":\"http://heroku-voip.marksilver.net/post_audio_to_s3?filename=foo.wav&unique_id=bar\"}},{\"say\":[{\"value\":\"You are now on the record.\"}]},{\"say\":[{\"value\":\"Go ahead, sing-along.\"}]},{\"say\":[{\"value\":\"http://denalidomain.com/music/keepers/HappyHappyBirthdaytoYou-Disney.mp3\"}]}]}"
	}
	
	/*
	public void testGenerateVoiceSession() {
		
		def builder = new TropoBuilder()
		def json_session = "{\"session\":{\"id\":\"0-13c4-4b563da3-7aecefda-46af-1d10bdd0\",\"accountId\":\"33932\",\"timestamp\":\"2010-01-19T23:18:00.854Z\",\"userType\":\"HUMAN\",\"to\":{\"id\":\"9991427589\",\"name\":\"unknown\",\"channel\":\"VOICE\",\"network\":\"PSTN\"},\"from\":{\"id\":\"jsgoecke\",\"name\":\"unknown\",\"channel\":\"VOICE\",\"network\":\"PSTN\"}}}"
		def map = builder.parse(json_session)
		
		assert builder.voiceSession == true
		assert builder.textSession == false
	}
	
	public void testGenerateTextSession() {
		
		def builder = new TropoBuilder()
		def json_session = "{\"session\":{\"id\":\"dih06n\",\"accountId\":\"33932\",\"timestamp\":\"2010-01-19T23:18:48.562Z\",\"userType\":\"HUMAN\",\"to\":{\"id\":\"tropomessaging@bot.im\",\"name\":\"unknown\",\"channel\":\"TEXT\",\"network\":\"JABBER\"},\"from\":{\"id\":\"john_doe@gmail.com\",\"name\":\"unknown\",\"channel\":\"TEXT\",\"network\":\"JABBER\"}}}"
		def map = builder.parse(json_session)
		
		assert builder.voiceSession == true
		assert builder.textSession == false
	}	
	
	*/

	public void testCall() {
		
		def builder = new TropoBuilder()
		
		builder.call(to: 'foo', from: 'bar', network: 'SMS', channel: 'TEXT', timeout: 10, answerOnMedia: false) {
			headers(foo: 'foo', bar: 'bar')
			recording(url: 'http://foobar', method: 'POST', format: 'audio/mp3', username: 'jose', password: 'passwd')
		} 

		assert builder.text() == "{\"tropo\":[{\"call\":{\"to\":\"foo\",\"from\":\"bar\",\"network\":\"SMS\",\"channel\":\"TEXT\",\"timeout\":10,\"answerOnMedia\":false,\"headers\":{\"foo\":\"foo\",\"bar\":\"bar\"},\"recording\":{\"url\":\"http://foobar\",\"method\":\"POST\",\"format\":\"audio/mp3\",\"username\":\"jose\",\"password\":\"passwd\"}}}]}"
	}
	
	public void testMessage() {
		
		def builder = new TropoBuilder()
		
		builder.message(to: 'foo', from: 'bar', network: 'SMS', channel: 'TEXT', timeout: 10, answerOnMedia: false) {
			headers(foo: 'foo', bar: 'bar')
			recording(url: 'http://foobar', method: 'POST', format: 'audio/mp3', username: 'jose', password: 'passwd')
			say('Please say your account number')
		}

		assert builder.text() == "{\"tropo\":[{\"message\":{\"to\":\"foo\",\"from\":\"bar\",\"network\":\"SMS\",\"channel\":\"TEXT\",\"timeout\":10,\"answerOnMedia\":false,\"headers\":{\"foo\":\"foo\",\"bar\":\"bar\"},\"recording\":{\"url\":\"http://foobar\",\"method\":\"POST\",\"format\":\"audio/mp3\",\"username\":\"jose\",\"password\":\"passwd\"},\"say\":[{\"value\":\"Please say your account number\"}]}}]}"
	}
	
	public void testRecordWithTranscriptionRequest() {
		
		def builder = new TropoBuilder()
		
		builder.record(name: 'foo', url: 'http://sendme.com/tropo', beep: true, sendTones: true, exitTone: '#') {
			transcription(id: 'bling', url:'mailto:jose@voxeo.com', emailFormat: 'encoded')
			say('Please say your account number')
			choices(value: '[5 DIGITS]')
		}

		assert builder.text() == "{\"tropo\":[{\"record\":{\"name\":\"foo\",\"url\":\"http://sendme.com/tropo\",\"beep\":true,\"sendTones\":true,\"exitTone\":\"#\",\"transcription\":{\"id\":\"bling\",\"url\":\"mailto:jose@voxeo.com\",\"emailFormat\":\"encoded\"},\"say\":[{\"value\":\"Please say your account number\"}],\"choices\":{\"value\":\"[5 DIGITS]\"}}}]}"
	}
	
	
	public void testUseMapsInsteadOfMethods() {
		
		def builder = new TropoBuilder()
		
		def help_stop_choices = '0(0,help,i do not know, agent, operator, assistance, representative, real person, human), 9(9,quit,stop,shut up)'
		def yes_no_choices = 'true(1,yes,sure,affirmative), false(2,no,no thank you,negative),' + help_stop_choices
		builder.ask(name: 'donate_to_id', bargein: true, timeout: 10, silenceTimeout: 10, attempts: 4) {
			say([[event: 'timeout', value: 'Sorry, I did not hear anything.'], 
				[event: 'nomatch:1 nomatch:2 nomatch:3', value: "Sorry, that wasn't a valid answer. You can press or say 1 for 'yes', or 2 for 'no'."],
				[value: 'You chose organization foobar. Are you ready to donate to them? If you say no, I will tell you a little more about the organization.'],
				[event: 'nomatch:3', value: 'This is your last attempt.']])
			choices(value: yes_no_choices)

		}

		assert builder.text() == "{\"tropo\":[{\"ask\":{\"name\":\"donate_to_id\",\"bargein\":true,\"timeout\":10,\"silenceTimeout\":10,\"attempts\":4,\"say\":[{\"event\":\"timeout\",\"value\":\"Sorry, I did not hear anything.\"},{\"event\":\"nomatch:1 nomatch:2 nomatch:3\",\"value\":\"Sorry, that wasn't a valid answer. You can press or say 1 for 'yes', or 2 for 'no'.\"},{\"value\":\"You chose organization foobar. Are you ready to donate to them? If you say no, I will tell you a little more about the organization.\"},{\"event\":\"nomatch:3\",\"value\":\"This is your last attempt.\"}],\"choices\":{\"value\":\"true(1,yes,sure,affirmative), false(2,no,no thank you,negative),0(0,help,i do not know, agent, operator, assistance, representative, real person, human), 9(9,quit,stop,shut up)\"}}}]}"
	}
	
	public void testVoice() {
		
		def builder = new TropoBuilder()		
		assert builder.voice() == null
		
		builder = new TropoBuilder(voice: 'barnie')
		assert builder.voice() == 'barnie'
		
		builder = new TropoBuilder()
		builder.voice = 'barnie'
		assert builder.voice() == 'barnie'
	}
	
	
	public void testVoiceHandling() {
		
		def builder = new TropoBuilder()
		builder.say('Hi There')		
		assert builder.json().tropo[0].say[0].voice == null
		
		builder = new TropoBuilder()
		builder.say('Hi there', voice: 'barnie')
		assert builder.json().tropo[0].say[0].voice == 'barnie'
		
		builder = new TropoBuilder(voice: 'barnie')
		builder.say('Hi There')
		builder.say('Wow!')
		assert builder.json().tropo[0].say[0].voice == 'barnie'
		assert builder.json().tropo[1].say[0].voice == 'barnie'

		builder = new TropoBuilder(voice: 'barnie')
		builder.say('Hi There')
		builder.say('Wow!', voice: 'jack')
		assert builder.json().tropo[0].say[0].voice == 'barnie'
		assert builder.json().tropo[1].say[0].voice == 'jack'
		
		builder = new TropoBuilder()
		builder.voice = 'barnie'
		builder.say('Hi There')
		builder.say('Wow!', voice: 'jack')
		assert builder.json().tropo[0].say[0].voice == 'barnie'
		assert builder.json().tropo[1].say[0].voice == 'jack'
	}
	
	public void testRecognizer() {
		
		def builder = new TropoBuilder()
		assert builder.recognizer() == null
		
		builder = new TropoBuilder(recognizer: 'fr-fr')
		assert builder.recognizer() == 'fr-fr'
		
		builder = new TropoBuilder()
		builder.recognizer = 'fr-fr'
		assert builder.recognizer() == 'fr-fr'
	}
	
	public void testRecognizerHandling() {
		
		def builder = new TropoBuilder()
		builder.ask(name: 'foo', bargein: true, timeout: 30, required: true)
		assert builder.json().tropo[0].ask.recognizer == null 
		
		builder = new TropoBuilder(recognizer: 'fr-fr')
		builder.ask(name: 'foo', bargein: true, timeout: 30, required: true)
		assert builder.json().tropo[0].ask.recognizer == 'fr-fr'

		builder = new TropoBuilder(recognizer: 'fr-fr')
		builder.recognizer = 'fr-fr'
		builder.ask(name: 'foo', bargein: true, timeout: 30, required: true)
		assert builder.json().tropo[0].ask.recognizer == 'fr-fr'

		builder = new TropoBuilder(recognizer: 'fr-fr')
		builder.recognizer = 'fr-fr'
		builder.ask(name: 'foo', bargein: true, timeout: 30, required: true)
		builder.ask(name: 'foo', bargein: true, timeout: 30, required: true, recognizer: 'de-de')
		assert builder.json().tropo[0].ask.recognizer == 'fr-fr'
		assert builder.json().tropo[1].ask.recognizer == 'de-de'
	}
	
	public void testShouldParseJsonStringsAndMaps() {
		
		def json_session = "{\"session\":{\"id\":\"dih06n\",\"accountId\":\"33932\",\"timestamp\":\"2010-01-19T23:18:48.562Z\",\"userType\":\"HUMAN\",\"to\":{\"id\":\"tropomessaging@bot.im\",\"name\":\"unknown\",\"channel\":\"TEXT\",\"network\":\"JABBER\"},\"from\":{\"id\":\"john_doe@gmail.com\",\"name\":\"unknown\",\"channel\":\"TEXT\",\"network\":\"JABBER\"}}}"
		
		def map = new TropoBuilder().parse(json_session)
		assert map.session.userType == 'HUMAN' 

		map = new TropoBuilder().parse(map)
		assert map.session.userType == 'HUMAN'
	}
	
	
	public void testAppendTropo() {
		
		def builder1 = new TropoBuilder()
		builder1.tropo {
			say('Please say your account number')
		}
		
		def builder2 = new TropoBuilder()
		builder2.tropo {
			ask(name : 'foo', bargein: true, timeout: 30, required: true, choices: '[5 DIGITS]') {
				append(builder1)
			}
		}
	
		assert builder2.text() == "{\"tropo\":[{\"ask\":{\"name\":\"foo\",\"bargein\":true,\"timeout\":30,\"required\":true,\"choices\":[\"5 DIGITS\"],\"say\":[{\"value\":\"Please say your account number\"}]}}]}"
	}
	
	public void testAppendOnBlock() {
		
		def builder1 = new TropoBuilder() 
		builder1.tropo {
			on(event:'success',next:'/result.json')
		}
		
		def builder2 = new TropoBuilder()
		builder2.tropo {
			ask(name : 'foo', bargein: true, timeout: 30, required: true) {
				say('Please say your account number')
				choices(value: '[5 DIGITS]')
			}
			append(builder1)
		}
		assert builder2.text() == "{\"tropo\":[{\"ask\":{\"name\":\"foo\",\"bargein\":true,\"timeout\":30,\"required\":true,\"say\":[{\"value\":\"Please say your account number\"}],\"choices\":{\"value\":\"[5 DIGITS]\"}}},{\"on\":{\"event\":\"success\",\"next\":\"/result.json\"}}]}"
	}
}