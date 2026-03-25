using System;
using System.Collections.Generic;
using System.Globalization;
using NUnit.Framework;

namespace AutomatedTests.LW2
{
    public interface ILogger { void Log(string message); }
    public class FakeLogger : ILogger { public void Log(string message) { } }

    public class Calculator
    {
        private readonly ILogger _logger;
        public Calculator(ILogger logger) { _logger = logger; }

        public double Execute(string num1Str, string op, string num2Str)
        {
            if (!double.TryParse(num1Str, NumberStyles.Any, CultureInfo.InvariantCulture, out double n1) ||
                !double.TryParse(num2Str, NumberStyles.Any, CultureInfo.InvariantCulture, out double n2))
                throw new ArgumentException("Error: Invalid number format");

            double result = op switch
            {
                "+" => n1 + n2,
                "-" => n1 - n2,
                "*" => n1 * n2,
                "/" => n2 != 0 ? n1 / n2 : throw new DivideByZeroException("Error: Division by zero"),
                _ => throw new InvalidOperationException("Error: Unknown operation")
            };
            _logger.Log($"Операція: {n1} {op} {n2} = {result}");
            return result;
        }
    }

    [TestFixture]
    public class Lab2Tests
    {
        private Calculator _calc;
        [SetUp] public void Setup() => _calc = new Calculator(new FakeLogger());

        [TestCase("-5", "+", "-10", -15.0)]
        [TestCase("-20", "/", "-4", 5.0)]
        public void NegativeValuesTest(string n1, string op, string n2, double expected) =>
            Assert.AreEqual(expected, _calc.Execute(n1, op, n2));

        private static IEnumerable<TestCaseData> LargeNumbersData()
        {
            yield return new TestCaseData("1.0e15", "+", "1.0e15").Returns(2.0e15);
            yield return new TestCaseData("1.79e308", "+", "0").Returns(1.79e308);
        }

        [Test, TestCaseSource(nameof(LargeNumbersData))]
        public double LargeNumbersTest(string n1, string op, string n2) => _calc.Execute(n1, op, n2);
    }

    public class PasswordChecker
    {
        public bool IsValid(string password)
        {
            if (string.IsNullOrEmpty(password) || password.Length < 8) return false;
            bool hasDigit = false;
            foreach (var c in password) { if (char.IsDigit(c)) hasDigit = true; }
            return hasDigit;
        }
    }

    [TestFixture]
    public class PasswordTests
    {
        [TestCase("12345", false)]
        [TestCase("password", false)]
        [TestCase("admin123", true)]
        public void TestPassword(string pwd, bool expected)
        {
            var checker = new PasswordChecker();
            Assert.AreEqual(expected, checker.IsValid(pwd));
        }
    }
}