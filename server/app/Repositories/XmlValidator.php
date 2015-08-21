<?php

namespace App\Repositories;

use App\Interfaces\XmlValidator as XmlValidatorInterface;

class XmlValidator implements XmlValidatorInterface
{
    /*
    * Checks whether the xml metadata matches the rules or not
    */
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

    /*
    * Validates the xml against the ExARML Schema
    */
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

    /*
    * Checks if the media files out of the xml matches the media files stored on the server
    */
    public function validateXmlMediaFiles($id, $xml)
    {
        $retVal = null;

        $xmlParser = new XmlParser();
        $xmlMediaFiles = $xmlParser->getXmlMediaFiles($xml);

        $allFiles = array();

        foreach (\Storage::allFiles($id) as $fileData) {
            $fileInfo = pathinfo($fileData);
            $allFiles[] = $fileInfo['basename'];
        }

        $diff = array_diff($xmlMediaFiles, $allFiles);

        if (empty($diff)) {
            $retVal = true;
        } else {
            $retVal = false;
        }

        return $retVal;
    }

}