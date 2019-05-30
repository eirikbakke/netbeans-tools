<?php

namespace Synergy\Controller\Test;

use Synergy\Controller\CaseCtrl;
use Synergy\Controller\LabelCtrl;
use Synergy\DB\Test\FixtureTestCase;

/**
 * Generated by PHPUnit_SkeletonGenerator 1.2.0 on 2013-11-12 at 15:54:46.
 */
class LabelCtrlTest extends FixtureTestCase {

    /**
     * @var LabelCtrl
     */
    protected $object;
    public static $extraLabel = false;

    /**
     * Sets up the fixture, for example, opens a network connection.
     * This method is called before a test is executed.
     */
    public function setUp() {
        $this->object = new LabelCtrl;
        parent::setUp();
    }

    public function testFindMatchingLabels() {
        $caseCtrl = new CaseCtrl();
        $caseCtrl->createGetKeyword('foo');
        $matched = $this->object->findMatchingLabels('sa');
        $this->assertEquals(1, count($matched));
        $this->assertEquals('sanity', $matched[0]->label);
        LabelCtrlTest::$extraLabel = true;
    }

    public function testGetAllLabels() {
        $all = $this->object->getAllLabels();
        $this->assertEquals(1, count($all));
    }

}
