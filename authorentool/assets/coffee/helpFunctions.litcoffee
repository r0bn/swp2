        btnSwitchDown = (buttonID, currentObjID) ->
            $(buttonID).click ->
                nextObject = $(currentObjID).next()
                currentObject = $(currentObjID)
                if typeof nextObject.attr("id") == 'undefined' || nextObject.prop('tagName') != currentObject.prop('tagName')
                    currentObject.effect("shake")
                    return
                else
                    currentObject.animate {opacity: 0.25}, 400, () ->
                        nextObject.insertBefore("#" + currentObject.attr("id"))
                    currentObject.animate {opacity: 1}, 500
                    return
                return
            return

        btnSwitchUp = (buttonID, currentObjID) ->
            $(buttonID).click ->
                prevObject = $(currentObjID).prev()
                currentObject = $(currentObjID)
                if typeof prevObject.attr("id") == 'undefined' || prevObject.prop('tagName') != currentObject.prop('tagName')
                    currentObject.effect("shake")
                    return
                else
                    currentObject.animate {opacity: 0.25}, 400, () ->
                        prevObject.insertAfter("#" + currentObject.attr("id"))
                    currentObject.animate {opacity: 1}, 500
                    return
                return
            return

        btnEinklappen = (button, content) ->
            # Click Event für btnQuizEinklappen
            $(button).click ->
                if $(content).is( ":hidden" )
                    $(content).show( "slow" )
                    $(button).find("span").addClass('glyphicon-resize-small')
                    $(button).find("span").removeClass('glyphicon-resize-full')
                    return
                else
                    $(content).slideUp("slow")
                    $(button).find("span").removeClass('glyphicon-resize-small')
                    $(button).find("span").addClass('glyphicon-resize-full')
                    return
            return

        setIDs = (node, counter) ->
            node.children().each () ->
                if typeof $(this).attr("id") != "undefined"
                    newID =  $(this).attr("id") + "_" + counter
                    $(this).attr("id", newID)
                    if typeof $(this).attr("for") != "undefined"
                        newFor = $(this).attr("for") + "_" + counter
                        $(this).attr("for", newFor)
                setIDs($(this), counter)
            return
            


        baueXML = () ->
            xml = '<?xml version="1.0" encoding="UTF-8"?>\r\n'
            xml += '<arml xmlns="http://www.opengis.net/arml/2.0"\r\n'
            xml += '    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"\r\n'
            xml += '    xmlns:xlink="http://www.w3.org/1999/xlink"\r\n'
            xml += '    xmlns:gml="http://www.opengis.net/gml/3.2"\r\n'
            xml += '    xsi:schemaLocation="http://www.opengis.net/arml/2.0 schema/ExARML.xsd">\r\n'
            xml += '</arml>'
            xml += '    <Story>\r\n'
            xml += '        <Revision>7</Revision>\r\n'
            xml += '        <Title> </Title>\r\n'
            xml += '        <Description> </Description>\r\n'
            xml += '        <Author> </Author>\r\n'
            xml += '        <Size uom="kb"> 0 </Size>\r\n'
            xml += '        <Location>\r\n'
            xml += '            <gml:Point gml:id="Location">\r\n'
            xml += '                <gml:pos> 0 0 </gml:pos>\r\n'
            xml += '            </gml:Point>\r\n'
            xml += '        </Location>\r\n'
            xml += '        <Radius uom="m">0</Radius>\r\n'
            xml += '    </Story>\r\n'
            return xml
