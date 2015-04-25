<?php

class ExampleTest extends TestCase {

	/**
	 * A basic functional test example.
	 *
	 * @return void
	 */
	public function testBasicExample()
	{
		$response = $this->call('GET', '/');

		$this->assertEquals(200, $response->getStatusCode());
	}

    public function testGetStoriesJson()
    {
        $response = $this->action('GET', 'StorytellarController@getStories', array(), array(), array(), array('HTTP_Accept' => 'application/json'));
        $this->assertResponseStatus(200);
        // I just needed to access the public headers var (which is a Symfony ResponseHeaderBag object)
        $this->assertEquals('application/json', $response->headers->get('Content-Type'));
    }

}
