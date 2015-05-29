<?php

class JsonTest extends TestCase {

    public function testGetStoriesJson()
    {
//        $response = $this->action('GET', 'StorytellarController@getStories', array(), array(), array(), array('HTTP_Accept' => 'application/json'));
//        $this->assertResponseStatus(200);
//        $this->assertEquals('application/json', $response->headers->get('Content-Type'));

        $this->call('GET', '/story');

        $this->assertResponseOk();
    }

}
