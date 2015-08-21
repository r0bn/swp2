<?php

namespace App\Interfaces;

interface XmlValidator
{
    public function validateXmlMetadata($xmlMetadata);

    public function validateXmlSchema($xml);
}