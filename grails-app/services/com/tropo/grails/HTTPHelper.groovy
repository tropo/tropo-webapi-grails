package com.tropo.grails

import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import net.sf.json.JSONArray

import static groovyx.net.http.Method.*

/**
 * <p>Abstracts all HTTP access from our REST API client layer. This library uses Groovy HTTPBuilder to execute all 
 * the REST methods.</p>
 *
 * <p>Tropo REST API requires basic authentication to execute some of its methods. Therefore the constructor of this 
 * class requires an username and password to be passed into</p>
 *
 * @author martin
 *
 */
class HTTPHelper {

    def username
    def password

    /**
     * Creates an instance of this class with the given basic auth params
     *
     * @param username Basic auth username
     * @param password Basic auth password
     */
    public HTTPHelper(username, password) {

        this.username = username
        this.password = password
    }

    /**
     * Executes a GET operation
     *
     * @param url URL
     * @param params Map with parameters. This map will be sent in the request body
     *
     * @return Response. It comes in JSON format
     */
    def Object doGet(url, params) {

        return doRequest(url, params, GET)
    }

    /**
     * Executes a DELETE operation
     *
     * @param url URL
     * @param params Map with parameters. This map will be sent in the request body
     *
     * @return Response. It comes in JSON format
     */
    def Object doDelete(url, params) {

        return doRequest(url, params, DELETE)
    }

    /**
     * Executes a PUT operation
     *
     * @param url URL
     * @param params Map with parameters. This map will be sent in the request body
     *
     * @return Response. It comes in JSON format
     */
    def Object doPut(url, params) {

        return doRequest(url, params, PUT)
    }

    /**
     * Executes a POST operation
     *
     * @param url URL
     * @param params Map with parameters. This map will be sent in the request body
     *
     * @return Response. It comes in JSON format
     */
    def Object doPost(url, params) {

        return doRequest(url, params, POST)
    }

    def Object doRequest(url, params, method) {

        //println "Requesting url ${url}"
        def result
        def http = new HTTPBuilder(url)
        if (username && password) {
            http.auth.basic username, password
        }
        http.request(method) {
            headers.'User-Agent' = 'Mozilla/5.0 Ubuntu/8.10 Firefox/3.0.4'
            headers.'Accept' = 'application/json'
            if (method == PUT) {
                headers.'Content-Type' = 'application/json'
            } else {
                requestContentType = ContentType.URLENC
            }
            if ((method == POST || method == GET) && params) {
                body = params
                //println params
            }
            response.success = { resp, json ->
                assert resp.statusLine.statusCode == 200
                result = json
            }
            response.failure = { resp ->
                result = [error: "Unexpected error: ${resp.statusLine.statusCode} : ${resp.statusLine.reasonPhrase}",
                          code : resp.statusLine.statusCode, reason: resp.statusLine.reasonPhrase, success: false, url: url]
            }
        }

        // Handle the failure condition
        if (result instanceof Map) {
            result.put "requestParams", params
            if (result.href) {
                populateExtraMapFields('appId', 'applications', result)
                populateExtraMapFields('numberId', 'number', result)
                populateExtraMapFields('token', 'token', result)
            }
        }
        //println result
        return result
    }

    private void populateExtraMapFields(String key, String pattern, Map result) {

        int i = result.href.indexOf(pattern + '/')
        def length = pattern.length() + 1
        if (i != -1) {
            int j = result.href.indexOf('/', i + length)
            if (j != -1) {
                result.put key, result.href.substring(i + length, j)
            } else {
                result.put key, result.href.substring(i + length)
            }
        }
    }
}
