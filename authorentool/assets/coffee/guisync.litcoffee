        startSynchro = () ->
            xml = '<?xml version="1.0" encoding="UTF-8"?>\n'
            xml += '<arml xmlns="http://www.opengis.net/arml/2.0"\n'
            xml += 'xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"\n'
            xml += 'xmlns:xlink="http://www.w3.org/1999/xlink"\n'
            xml += 'xmlns:gml="http://www.opengis.net/gml/3.2"\n'
            xml += 'xsi:schemaLocation="http://www.opengis.net/arml/2.0 schema/ExARML.xsd">\n'
            xml = synchronizeStoryTag(xml)
            xml = synchronizeDependencyTag(xml)
            return xml
            
        synchronizeStoryTag = (xml) ->
            xml += '<Story>\n'
            xml += '    <Revision>7</Revision>\n'
            xml += '    <Title>' + synchronizeInputHelper("inTitel") + '</Title>\n'
            xml += '    <Description>' + synchronizeInputHelper("ttaDescription") + '</Description>\n'
            xml += '    <Author>' + synchronizeInputHelper("inAutor") + '</Author>\n'
            xml += '    <Size uom="' + synchronizeInputHelper("ddnsize") + '">' + synchronizeInputHelper("inSize") + '</Size>\n'
            xml += '    <Location>\n'
            xml += '        <gml:Point gml:id="Location">\n'
            xml += '            <gml:pos>' + synchronizeGPSCalc("inLatLngLocation") + '</gml:pos>\n'
            xml += '        </gml:Point>\n'
            xml += '    </Location>\n'
            xml += '    <Radius uom="' + synchronizeInputHelper("ddnradius") + '">' + synchronizeInputHelper("inRadius") + '</Radius>\n'
            xml += '</Story>\n'
            return xml

        synchronizeDependencyTag = (xml) ->
            xml += '<Dependency>\n'
                # Zu jedem Storypoint wechseln und durchlaufen
            $('#fhlStorypoints').find('.form-horizontal').each ->
                id = $(this).attr("id")
                id = id.split("_")
                xml += '<Storypoint id="'+ synchronizeInputHelper("inStorypoint_"+ id[1]) + '" >\n'
                xml += '    <FeatureRef xlink:href="#' + synchronizeInputHelper("inStorypoint_"+ id[1]) + '_Feature" />\n'
                xml += '    <Metadata>\n'
                xml += '        <Accessible>'+ $("#inInternet_" + id[1]).is( ":checked" ) + '</Accessible>\n'
                xml += '        <Internet>'+ $("#inInternet_" + id[1]).is( ":checked" )+ '</Internet>\n'
                xml += '    </Metadata>\n'
                xml += '    <Container>\n'
                xml += '        <Storypointlist />\n'
                xml += '        <Itemlist />\n'
                xml += '    </Container>\n'
                if $("#inEndOfStory_" + id[1]).is( ":checked" ) == true
                    xml += '    <EndOfStory>true</EndOfStory>\n'
                xml += '</Storypoint>\n'
            xml += '</Dependency>\n'
            return xml
            
        synchronizeGPSCalc = (inputID) ->
            return $("#"+inputID).val().replace ","," "
            
        synchronizeInputHelper = (inputID) ->
            if $("#"+inputID).val() != ""
                return $("#"+inputID).val()
            else
                return $("#"+inputID).attr("placeholder")
