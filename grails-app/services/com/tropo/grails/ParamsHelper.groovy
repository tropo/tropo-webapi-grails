package com.tropo.grails

/**
 * This class validates that the parameters sent to the Tropo REST API are appropiate
 *
 * @author martin
 *
 */
class ParamsHelper {

    /**
     * <p>Checks that parameter constraints are met. This method receives a map of parameters and a list of mandatory
     * parameters to check. If any of the specified parameters is not found within the map then a Tropo Exception will
     * be thrown.</p>
     *
     * <p>This method also checks the map to search for invalid values for the parameters within.</p>
     *
     * @param toCheck List of mandatory parameters
     * @param params Map with all the parameters to be checked
     *
     * @throws TropoException If a constraint is not met
     */
    def checkParams(List toCheck, Map params) {

        // Check mandatory parameters
        toCheck.each {
            if (!params[it]) {
                throw new TropoException("Missing parameter '${it}'")
            }
        }

        // Check mandatory values
    }

    /**
     * Returns the value of the specified key and removes it from the map passed as a parameter
     *
     * @param key Key
     * @param params Map to search in. The entry found will be removed from this map
     *
     * @return String Value for the key
     */
    def getAndRemove(String key, Map params) {

        def value = params[key]
        if (value) {
            value = value.trim()
        }
        params.remove key
        return value
    }
}
