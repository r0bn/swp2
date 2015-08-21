<?php

namespace App\Interfaces;

interface XmlParser
{
    public function getXmlMetadata($xmlString);

    public function getXmlMediaFiles($xmlString);
}