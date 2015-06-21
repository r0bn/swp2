<?php namespace App\Http\Controllers;

use Illuminate\Http\Request;
//use Illuminate\Routing\Controller;
use App\Http\Controllers\Controller;

use App\Interfaces\AuthHelper as AuthHelperInterface;
use App\Interfaces\FileHelper as FileHelperInterface;
use App\Interfaces\Story as StoryInterface;
use App\Interfaces\TempStory as TempStoryInterface;
use App\Interfaces\XmlParser as XmlParserInterface;
use App\Interfaces\XmlValidator as XmlValidatorInterface;

class StorytellarController extends Controller
{

    protected $authHelper;
    protected $fileHelper;
    protected $story;
    protected $tempStory;
    protected $xmlParser;
    protected $xmlValidator;

    public function __construct(AuthHelperInterface $authHelper,
                                FileHelperInterface $fileHelper,
                                StoryInterface $story,
                                TempStoryInterface $tempStory,
                                XmlParserInterface $xmlParser,
                                XmlValidatorInterface $xmlValidator)
    {
        $this->authHelper = $authHelper;
        $this->fileHelper = $fileHelper;
        $this->story = $story;
        $this->tempStory = $tempStory;
        $this->xmlParser = $xmlParser;
        $this->xmlValidator = $xmlValidator;
    }


    public function getStories(Request $request)
    {
        $retVal = null;

        if ($request->all()) {
            $retVal = $this->story->getFilteredStories($request);
        } else {
            $retVal = $this->story->getAllStories();
        }

        return $retVal;
    }

    public function getUserStories()
    {
        $token = \JWTAuth::getToken();
        $user = \JWTAuth::toUser($token);

        return $this->story->getUserStories($user->id);
    }

    public function getOpenStories()
    {
        return $this->story->getAllOpenStories();
    }

    public function getStory($id)
    {
        return $this->story->getStory($id);
    }

    public function getStoryMediaFiles($id)
    {
        return $this->fileHelper->getStoryMediaFiles($id);
    }

    public function createStory(Request $request)
    {
        $retVal = null;
        $userId = null;

        $workingTitle = $request->input('working_title');
        $xmlString = $request->input('xml');

        if ($request->header('Authorization')) {
            $token = \JWTAuth::getToken();
            $user = \JWTAuth::toUser($token);

            $userId = $user->id;
        }

        $retVal = $this->story->createStorySlot($xmlString, $workingTitle, $userId);

        return $retVal;
    }

    public function updateStory(Request $request, $id)
    {
        if ($this->authHelper->checkUserValidation($id)) {
            $xmlString = $request->input('xml');

            if ($request->input('final') && $this->xmlValidator->validateXmlSchema($xmlString) && $this->xmlValidator->validateXmlMetadata($this->xmlParser->getXmlMetadata($xmlString)) && $this->xmlValidator->validateXmlMediaFiles($id, $xmlString)) {
                $this->story->updateStory($xmlString, $id);
            } else {
                $workingTitle = $request->input('working_title');
                $this->story->updateStorySlot($xmlString, $id, $workingTitle);
            }
        }
    }

    public function deleteStory($id)
    {
        if ($this->authHelper->checkUserValidation($id)) {
            return $this->story->deleteStory($id);
        }
    }

    public function addFile(Request $request, $id)
    {
        if ($this->authHelper->checkUserValidation($id)) {
            $file = $request->file('file');
            $this->fileHelper->storeStoryMediaFile($id, $file);

            $this->story->changeFinalStatus($id, false);
        }
    }

    public function deleteFile($id, $filename)
    {
        if ($this->authHelper->checkUserValidation($id)) {
            $this->fileHelper->deleteStoryMediaFile($id, $filename);

            $this->story->changeFinalStatus($id, false);
        }
    }


    /**
     * Temporary for player testing group.
     */

    public function getTempStories(Request $request)
    {
        $retVal = null;

        if ($request->all()) {
            $retVal = $this->tempStory->getFilteredStories($request);
        } else {
            $retVal = $this->tempStory->getAllStories();
        }

        return $retVal;
    }

    public function getTempStory($id)
    {
        return $this->tempStory->getStory($id);
    }


    /**
     * Temporary for xml schema validation testing.
     */

    public function getXmlSchemaValidation()
    {
        return \View::make('tests.xmlschemavalidation');
    }

    public function postXmlSchemaValidation(Request $request)
    {
        $xmlString = $request->input('xmlstring');

        $retVal = false;

        if ($xmlString != '' && $this->xmlValidator->validateXmlSchema($xmlString) && $this->xmlValidator->validateXmlMetadata($this->xmlParser->getXmlMetadata($xmlString))) {
            $retVal = true;
        }

        $headers['Content-Type'] = 'application/json; charset=utf-8';

        return response()->json($retVal, 200, $headers, JSON_UNESCAPED_UNICODE);
    }

}
