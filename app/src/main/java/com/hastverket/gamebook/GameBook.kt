package com.hastverket.gamebook

import java.io.IOException
import java.io.InputStream
import java.util.LinkedList

import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException

import org.w3c.dom.Document
import org.w3c.dom.NamedNodeMap
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import org.xml.sax.InputSource
import org.xml.sax.SAXException

import android.util.Log

class GameBook(stream: InputStream) {

    internal var mGameBookRooms = LinkedList<Room>()

    init {
        xmlToGameBook(xmlDocumentFromString(stream)!!)
    }

    fun getRoomWithId(id: String): Room? {
        for (r in mGameBookRooms) {
            if (r.mId?.compareTo(id) === 0) {
                return r
            }
        }

        return null
    }

    private fun xmlDocumentFromString(byteStream: InputStream): Document? {
        val docBuilder = DocumentBuilderFactory.newInstance()

        var document: Document? = null

        try {
            val builder = docBuilder.newDocumentBuilder()

            val `is` = InputSource(byteStream)
            document = builder.parse(`is`)

        } catch (e: ParserConfigurationException) {
            e.printStackTrace()
        } catch (e: SAXException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return document
    }

    private fun xmlToGameBook(xmlDoc: Document) {

        val nodes = xmlDoc.getElementsByTagName("room")
        val nbrOfRooms = nodes.length

        for (i in 0 until nbrOfRooms) {
            val n = nodes.item(i) as Node
            val nnm = n.attributes as NamedNodeMap
            var id: String? = null

            for (j in 0 until nnm.length) {
                val attribute = nnm.item(j)
                val name = attribute.nodeName
                val `val` = attribute.nodeValue

                if (name.compareTo("id", ignoreCase = true) == 0) {
                    id = `val`
                }
            }

            var description: String? = null
            val roomLinks = LinkedList<RoomLink>()

            val properties = n.childNodes
            for (j in 0 until properties.length) {
                val property = properties.item(j)
                val name = property.nodeName

                if (name.equals("description", ignoreCase = true)) {
                    description = property.firstChild.nodeValue
                } else if (name.equals("link", ignoreCase = true)) {
                    var ref: String? = null
                    var desc: String? = null

                    val linkAttrs = property.attributes as NamedNodeMap

                    for (k in 0 until linkAttrs.length) {
                        val attribute = linkAttrs.item(k)
                        val linkAttrName = attribute.nodeName
                        val linkAttrVal = attribute.nodeValue

                        if (linkAttrName.equals("ref", ignoreCase = true)) {
                            ref = linkAttrVal
                        }
                        if (linkAttrName.equals("description", ignoreCase = true)) {
                            desc = linkAttrVal
                        }

                    }
                    Log.v(TAG, "room link ref: $ref desc: $desc")
                    val rl = RoomLink(ref!!, desc!!)
                    roomLinks.add(rl)
                }
            }

            val room = Room(id!!, description!!, roomLinks)
            mGameBookRooms.add(room)
        }
    }

    companion object {
        private val TAG = GameBook::class.java.simpleName
    }
}
