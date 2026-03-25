<h1>Welcome to calculator</h1>
<form id="calculator-form" method="post">
    <input name="number1" id="calculator-number1" placeholder="Число 1">
    <select name="operation">
        <option value="plus">+</option>
        <option value="minus">-</option>
        <option value="multiply">*</option>
        <option value="divide">/</option>
    </select>
    <input name="number2" id="calculator-number2" placeholder="Число 2">
    <button type="submit" id="calculator-submit">Calculate</button>
</form>

<?php
if (isset($_POST['number1']) && isset($_POST['number2'])) {
    $n1 = (float)$_POST['number1'];
    $n2 = (float)$_POST['number2'];
    $op = $_POST['operation'];
    $result = "";

    if ($op == 'plus') $result = $n1 + $n2;
    elseif ($op == 'minus') $result = $n1 - $n2;
    elseif ($op == 'multiply') $result = $n1 * $n2;
    elseif ($op == 'divide') {
        if ($n2 == 0) $result = "Error: Division by zero";
        else $result = $n1 / $n2;
    }

    echo "<div id='result'>Result: $result</div>";
}
?>