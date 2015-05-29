<?php

namespace App\Repositories;

use App\Interfaces\XmlValidator as XmlValidatorInterface;

class XmlValidator implements XmlValidatorInterface
{
    public function validateXmlMetadata($xmlMetadata)
    {
        $retVal = null;

        $validator = \Validator::make($xmlMetadata, [
            'title'       => 'required|min:1|max:255',
            'description' => 'required|min:1|max:65535',
            'author'      => 'required|min:1|max:255',
            'revision'    => 'required|integer',
            'size'        => 'required|integer',
            'size_uom'    => 'required|min:1|max:255',
            'location'    => 'required|regex:/^(-?\d{1,2}\.\d{6}) (-?\d{1,2}\.\d{6})$/',
            'radius'      => 'required|integer',
            'radius_uom'  => 'required|min:1|max:255',
        ]);

        if ($validator->fails()) {
            $retVal = false;
        } else {
            $retVal = true;
        }

        return $retVal;
    }

    public function validateXmlSchema($xml)
    {
        $retVal = null;

        putenv("XML_CATALOG_FILES=xml/schema/catalog.xml");

        $xsdfile = 'xml/schema/ExARML.xsd';

        libxml_use_internal_errors(true);

        $xmlValidator = new \DOMDocument();
        $xmlValidation = $xmlValidator->loadXML($xml);

        if ($xmlValidation && $xmlValidator->schemaValidate($xsdfile)) {
            $retVal = true;
        } else {
            $retVal = false;
        }

        return $retVal;
    }

}