<?php
require_once 'calculatorClass.php';

use PHPUnit\Framework\TestCase;

class CalculatorClassTest extends TestCase {
    private $calculator;

    protected function setUp(): void {
        $this->calculator = new CalculatorClass();
    }

    public function testAdd() {
        $this->assertEquals(15, $this->calculator->add(10, 5));
    }

    public function testAddNegative() {
        $this->assertEquals(-5, $this->calculator->add(5, -10));
    }

    public function testAddFloat() {
        $this->assertEqualsWithDelta(7.7, $this->calculator->addFloat(3.6, 4.1), 0.01);
    }

    public function testSubtract() {
        $this->assertEquals(5, $this->calculator->subtract(10, 5));
    }

    public function testExtractFloat() {
        $this->assertEqualsWithDelta(2.2, $this->calculator->subtract(5.4, 3.2), 0.01);
    }

    public function testMultiply() {
        $this->assertEquals(50, $this->calculator->multiply(10, 5));
    }

    public function testMultiplyFloat() {
        $this->assertEqualsWithDelta(16.12, $this->calculator->multiply(5.2, 3.1), 0.01);
    }

    public function testDivide() {
        $this->assertEquals(2, $this->calculator->divide(10, 5));
    }

    public function testDivideFloat() {
        $this->assertEquals(2.05, $this->calculator->divide(8.2, 4));
    }

    public function testDivideByZero() {
        $this->expectException(Exception::class);
        $this->calculator->divide(10, 0);
    }
}