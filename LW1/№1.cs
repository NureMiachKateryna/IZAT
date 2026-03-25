using NUnit.Framework;
using System;
using System.Globalization;

namespace AutomatedTests
{
    public class Calculator
    {
        public double Execute(string num1Str, string op, string num2Str)
        {
            if (!double.TryParse(num1Str, NumberStyles.Any, CultureInfo.InvariantCulture, out double n1) ||
                !double.TryParse(num2Str, NumberStyles.Any, CultureInfo.InvariantCulture, out double n2))
                throw new ArgumentException("Error: Invalid number format");

            return op switch
            {
                "+" => n1 + n2,
                "-" => n1 - n2,
                "*" => n1 * n2,
                "/" => n2 != 0 ? n1 / n2 : throw new DivideByZeroException("Error: Division by zero"),
                _ => throw new InvalidOperationException("Error: Unknown operation")
            };
        }
    }

    [TestFixture]
    public class CalculatorAutomationTests
    {
        private Calculator _calc;

        [SetUp]
        public void Setup()
        {
            _calc = new Calculator();
        }

        [Test]
        public void TC1_PositiveAddition()
        {
            double result = _calc.Execute("5", "+", "10");
            Assert.AreEqual(15.0, result);
        }

        [Test]
        public void TC2_PositiveDivisionFloat()
        {
            double result = _calc.Execute("10", "/", "4");
            Assert.AreEqual(2.5, result);
        }

        [Test]
        public void TC3_NegativeDivisionByZero()
        {
            var ex = Assert.Throws<DivideByZeroException>(() => _calc.Execute("10", "/", "0"));
            Assert.AreEqual("Error: Division by zero", ex.Message);
        }

        [Test]
        public void TC4_NegativeInvalidFormat()
        {
            var ex = Assert.Throws<ArgumentException>(() => _calc.Execute("abc", "+", "5"));
            Assert.AreEqual("Error: Invalid number format", ex.Message);
        }

        [Test]
        public void TC5_NegativeUnknownOperation()
        {
            var ex = Assert.Throws<InvalidOperationException>(() => _calc.Execute("10", "?", "2"));
            Assert.AreEqual("Error: Unknown operation", ex.Message);
        }

        [Test]
        public void TC6_BoundaryLargeNumbers()
        {
            double result = _calc.Execute("1.79e308", "*", "2");
            Assert.IsTrue(double.IsInfinity(result));
        }
    }
}