package com.tropo.grails

import grails.util.GrailsWebUtil
import net.sf.json.JSONArray
import net.sf.json.JSONObject;

import javax.servlet.http.HttpServletResponse

class TropoBuilder extends BuilderSupport {

    def root
    def currentNode
    def stack

    def voice
    def recognizer

    public TropoBuilder() {}

    public TropoBuilder(Map map) {

        if (map.voice) {
            voice = map.voice
        }
        if (map.recognizer) {
            recognizer = map.recognizer
        }
    }

    def voice() {

        return voice
    }

    def recognizer() {

        return recognizer
    }

    def wrap = { key, buffer ->

        return "{ \"${key}\" : [${buffer}] }"
    }

    def text() {

        if (!root) {
            throw new TropoBuilderException("You need a tropo root node!")
        }
        //println root.toString()
        return root.toString()
    }


    def json() {

        if (!root) {
            throw new TropoBuilderException("You need a tropo root node!")
        }
        return root
    }

    @Override
    protected Object createNode(Object name, Map map, Object more) {

        //println "Invoking createNode(object name, Map map, Object more) with args ${name} and ${map} and ${more}"

        if (name == "say" && more instanceof String) {
            map.put 'value', more
            def node = createNode(name, map)
            return
        }
        def node = createNode(name, map)

        if (more instanceof JSONArray) {
            more.each {

            }
        } else {
            map = cleanGStrings(map)
            def newNode = new JSONObject()
            newNode.putAll more
            validate(name, newNode)
            node.accumulate(name, newNode)
        }
        //println node
    }

    @Override
    protected Object createNode(Object name, Map map) {

        if (name == "say" || name == "ask") {
            if (voice && !map.voice) {
                map.put 'voice', voice
            }
            if (recognizer && !map.recognizer) {
                map.put 'recognizer', recognizer
            }
        }
        try {
            def created
            //println "Invoking createNode(object arg0, Map arg1) with args ${name} and ${map} / Current: ${getCurrent()}"
            if (name == "say") {
                created = buildArray(name, map)
            } else if (name == "on") {
                // it looks like "on" uses an array only if nested
                if (stack.peek() == root.tropo) {
                    created = internalBuildElement(name, map)
                } else {
                    created = buildArray(name, map)
                }
            } else {
                created = internalBuildElement(name, map)
            }
            return created;
        } catch (TropoBuilderException tbe) {
            throw tbe
        } catch (Exception e) {
            e.printStackTrace()
        }
    }

    private JSONObject internalBuildElement(String name, Map map) {

        //println "I would now insert node ${name} in stack element ${stack.peek()}"
        map = cleanGStrings(map)
        def node = stack.peek()

        def element = new JSONObject()
        if (node.isArray()) {
            node.element(element)
        } else {
            element = node
        }
        element.put(name, new JSONObject())

        if (name == "choices") {
            // There is an issue with JSON-LIB. When an element starts with { or [ then json-lib handles it
            // as JSON creating wrong text. Ej. "[5 DIGITS]" is transformed to ["5 DIGITS"]
            map.each {
                if (it.value.startsWith("[") || it.value.startsWith("{")) {
                    it.value = " " + it.value
                }
            }
            element[name].putAll map
            element[name].each {
                if (it.value.startsWith(" [") || it.value.startsWith(" {")) {
                    it.value = it.value.substring(1)
                }
            }
        } else {
            element[name].putAll map
        }


        validate(name, element[name])

        stack.push(element.get(name))

        return element
    }

    private JSONObject buildArray(String name, Map map) {

        map = cleanGStrings(map)
        //println "I would now insert node ${name} in stack element ${stack.peek()}"
        def node = stack.peek()
        def created

        def newNode = new JSONObject()
        newNode.putAll map
        validate(name, newNode)
        created = newNode

        if (!node[name]) {
            def array = new JSONArray()
            array.element(newNode)
            newNode = array
            created = newNode
        } else {
            if (name == 'say') {
                def array = new JSONArray()
                array.element(newNode)
                newNode = array
                created = newNode
            }
            //println 'Exists! skipping'
        }

        if (node.isArray()) {
            def holder = new JSONObject()
            holder.put name, newNode
            node.element(holder)
            created = holder
        } else {
            node.accumulate(name, newNode)
            created = node
        }

        def tostack = node[name]
        if (tostack instanceof JSONArray) {
            stack.push tostack[tostack.size() - 1]
        } else {
            stack.push(tostack)
        }

        return created
    }

    private void validate(String action, JSONObject node) throws TropoBuilderException {

        if (action == "say") {
            if (!node.value) {
                throw new TropoBuilderException("Missing required property: 'value'")
            }
        } else if (action == "ask") {
            if (!node.name) {
                throw new TropoBuilderException("Missing required property: 'name'")
            }
        } else if (action == "conference") {
            if (!node.name) {
                throw new TropoBuilderException("Missing required property: 'name'")
            }
            if (!node.id) {
                throw new TropoBuilderException("Missing required property: 'id'")
            }
        } else if (action == "record") {
            if (!node.name) {
                throw new TropoBuilderException("Missing required property: 'name'")
            }
            def url = node.url
            if (!url) {
                throw new TropoBuilderException("Missing required property: 'url'")
            }
            try {
                new URL(node.url)
            } catch (Exception e) {
                throw new TropoBuilderException("The 'url' parameter must be a valid URL")
            }
            if (url.startsWith("mailto:")) {
                node.put("url", url.substring(7))
            }
        } else if (action == "redirect") {
            if (stack.size() > 1) {
                throw new TropoBuilderException("Redirect should only be used alone and before the session is answered, use transfer instead")
            }
            if (!node.to) {
                throw new TropoBuilderException("Missing required property: 'to'")
            }
        } else if (action == "transfer") {
            if (!node.to) {
                throw new TropoBuilderException("Missing required property: 'to'")
            }
        } else if (action == "on") {
            if (!node.event) {
                throw new TropoBuilderException("Missing required property: 'event'")
            }
        } else if (action == "choices") {
            if (node.mode) {
                if (node.mode != 'dtmf' && node.mode != 'speech') {
                    throw new TropoBuilderException("If mode is provided, only 'dtmf', 'speech' or 'any' is supported")
                }
            }
        }
    }

    @Override
    protected Object createNode(Object name, Object value) {

        //println "Invoking createNode(object arg0, object arg1) with args ${name} and ${value}. Current: ${getCurrent()}"
        value = cleanGStrings(value)

        if (name == "say") {
            if (value instanceof Object[]) {
                def list = new ArrayList()
                value.each { list << it }
                value = list
            }
            if (!(value instanceof String) && !(value instanceof Map) && !(value instanceof List)) {
                throw new TropoBuilderException("An invalid paramater type ${value.getClass()} has been passed")
            }
            if (value instanceof List) {
                def node
                value.each {
                    if (it == value[0]) {
                        node = createNode(name, value[0])
                        //println node
                    } else {
                        def newNode = new JSONObject()
                        newNode.putAll it
                        validate(name, newNode)
                        node.accumulate(name, newNode)
                    }

                }
            } else {
                def map = ["value": value]
                createNode(name, map)
            }
        } else if (name == "append") {
            def builder = value.root['tropo']
            def rootArray = root['tropo']
            if (builder instanceof JSONObject) {
                rootArray.add(builder)
            } else if (builder instanceof JSONArray) {
                builder.each {
                    rootArray.add(it)
                }
            }
            stack.push root.tropo
        }
        return null;
    }

    @Override
    protected Object createNode(Object name) {

        //println "Invoking createNode(Object arg0) with args ${name} / Current: ${getCurrent()}"
        if (name == "tropo") {
            reset()
        } else {
            def node = stack.peek()
            def newNode = new JSONObject()
            newNode.put name, 'null'
            validate(name, newNode)
            node.element(newNode)
        }
        return null;
    }

    @Override
    protected void setParent(Object arg0, Object arg1) {

        //println "Invoking seParent(object arg0, Object arg1) / Current: ${getCurrent()}"
    }

    @Override
    protected Object doInvokeMethod(String arg0, Object arg1, Object arg2) {

        if (!root) {
            reset()
        }/*
		if (arg0.equals("say") && arg2 instanceof Object[] && arg2.length > 1) {
			def list = new ArrayList()
			list << arg2
			arg2 = list
		}*/
        return super.doInvokeMethod(arg0, arg1, arg2);
    }

    @Override
    protected void nodeCompleted(Object parent, Object node) {

        if (!stack.isEmpty()) {
            def pop = stack.pop()
            //println ("Got node ${pop} from the stack")
        }
        super.nodeCompleted(parent, node);
    }

    public Map parse(String json) {

        return JSONObject.fromObject(json)
    }

    public Map parse(Map json) {

        return JSONObject.fromObject(json)
    }

    public void render(HttpServletResponse response) {

        response.setContentType(GrailsWebUtil.getContentType("application/json", "UTF-8"));
        response.getWriter().write(text())
        response.getWriter().flush()
        response.getWriter().close()
    }

    public String toString() {

        return text()
    }

    public void reset() {

        stack = new Stack()
        root = new JSONObject()
        root.put("tropo", [])
        stack.push root.tropo
    }

    def isEmpty() {

        def node = root?.get("tropo")
        node == null || node.size() == 0
    }

    private Object cleanGStrings(Object value) {

        if (value instanceof GString) {
            return value as String
        } else if (value instanceof Map) {
            value.each {
                if (it.value instanceof GString) {
                    it.value = it.value as String
                }
            }
        } else if (value instanceof List) {
            value.each {
                if (it instanceof String) {
                    it = it as String
                }
            }
        }
        return value
    }


}
