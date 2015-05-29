<?php

namespace App\Repositories;

use App\Interfaces\XmlParser as XmlParserInterface;

class XmlParser implements XmlParserInterface
{
    public function getXmlMetadata($xmlString)
    {
        $xml = simplexml_load_string($xmlString);

        $xml->registerXPathNamespace('gml', 'http://www.opengis.net/gml/3.2');

        $xmlMetadata = array(
            'title'       => $xml->Story->Title->__toString(),
            'description' => $xml->Story->Description->__toString(),
            'author'      => $xml->Story->Author->__toString(),
            'revision'    => $xml->Story->Revision->__toString(),
            'size'        => $xml->Story->Size->__toString(),
            'size_uom'    => $xml->Story->Size['uom']->__toString(),
            'location'    => $xml->xpath('//gml:pos')[0]->__toString(),
            'radius'      => $xml->Story->Radius->__toString(),
            'radius_uom'  => $xml->Story->Radius['uom']->__toString(),
        );

        return $xmlMetadata;
    }


    public function getXmlMetadataSlot($xmlString)
    {
        $xmlMetadata = null;

        libxml_use_internal_errors(true);
        $xml = simplexml_load_string($xmlString);

        if (!count(libxml_get_errors())) {
            $xml->registerXPathNamespace('x', 'http://www.opengis.net/arml/2.0');
            $xml->registerXPathNamespace('gml', 'http://www.opengis.net/gml/3.2');

            $title = '';
            if (isset($xml->Story->Title)) $title = $xml->Story->Title->__toString();

            $description = '';
            if (isset($xml->Story->Description)) $description = $xml->Story->Description->__toString();

            $author = '';
            if (isset($xml->Story->Author)) $author = $xml->Story->Author->__toString();

            $revision = null;
            if (isset($xml->Story->Revision)) $revision = $xml->Story->Revision->__toString();

            $size = null;
            if (isset($xml->Story->Size)) $size = $xml->Story->Size->__toString();

            $sizeUom = '';
            if (isset($xml->Story->Size['uom'])) $sizeUom = $xml->Story->Size['uom']->__toString();

            $location = '';
            if (count($xml->xpath('//x:Story/x:Location/gml:Point/gml:pos'))) $location = $xml->Story->Location->xpath('//gml:pos')[0]->__toString();

            $radius = null;
            if (isset($xml->Story->Radius)) $radius = $xml->Story->Radius->__toString();

            $radiusUom = '';
            if (isset($xml->Story->Radius['uom'])) $radiusUom = $xml->Story->Radius['uom']->__toString();

            $xmlMetadata = array(
                'title'       => $title,
                'description' => $description,
                'author'      => $author,
                'revision'    => $revision,
                'size'        => $size,
                'size_uom'    => $sizeUom,
                'location'    => $location,
                'radius'      => $radius,
                'radius_uom'  => $radiusUom,
            );
        }

        return $xmlMetadata;
    }

}