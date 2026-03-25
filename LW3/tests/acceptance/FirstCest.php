<?php

class FirstCest
{
    public function checkPageOpens(AcceptanceTester $I)
    {
        $I->amOnPage('/');
        $I->see('Welcome to calculator');
    }

    public function checkFormElements(AcceptanceTester $I)
    {
        $I->amOnPage('/');
        $I->seeElement('#calculator-number1');
        $I->seeElement('#calculator-number2');
        $I->seeElement('#calculator-submit');
    }

    public function testAddition(AcceptanceTester $I)
    {
        $I->amOnPage('/');
        $I->fillField('number1', '10');
        $I->selectOption('operation', 'plus');
        $I->fillField('number2', '20');
        $I->click('#calculator-submit');
        $I->see('Result: 30', '#result');
    }

    public function testSubtraction(AcceptanceTester $I)
    {
        $I->amOnPage('/');
        $I->fillField('number1', '5');
        $I->selectOption('operation', 'minus');
        $I->fillField('number2', '15');
        $I->click('#calculator-submit');
        $I->see('Result: -10', '#result');
    }

    public function testMultiplication(AcceptanceTester $I)
    {
        $I->amOnPage('/');
        $I->fillField('number1', '4');
        $I->selectOption('operation', 'multiply');
        $I->fillField('number2', '5');
        $I->click('#calculator-submit');
        $I->see('Result: 20', '#result');
    }

    public function testDivision(AcceptanceTester $I)
    {
        $I->amOnPage('/');
        $I->fillField('number1', '10');
        $I->selectOption('operation', 'divide');
        $I->fillField('number2', '4');
        $I->click('#calculator-submit');
        $I->see('Result: 2.5', '#result');
    }

    public function testDivisionByZero(AcceptanceTester $I)
    {
        $I->amOnPage('/');
        $I->fillField('number1', '10');
        $I->selectOption('operation', 'divide');
        $I->fillField('number2', '0');
        $I->click('#calculator-submit');
        $I->see('Result: Error: Division by zero', '#result');
    }

    public function testFloatAddition(AcceptanceTester $I)
    {
        $I->amOnPage('/');
        $I->fillField('number1', '3.6');
        $I->selectOption('operation', 'plus');
        $I->fillField('number2', '4.1');
        $I->click('#calculator-submit');
        $I->see('Result: 7.7', '#result');
    }
}