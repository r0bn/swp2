<?php

class XmlTest extends TestCase {

    public function testGetStoryXml()
    {
//        $response = $this->action('GET', 'StorytellarController@getStory', array(), array(), array(), array('Accept' => 'text/xml'));
//        $this->assertResponseStatus(200);
//        $this->assertEquals('text/xml', $response->headers->get('Content-Type'));

        $this->call('GET', '/story/1');

        $this->assertResponseOk();
    }

}
