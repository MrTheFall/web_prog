<%@ page import="com.itmo.lab.models.CheckResult" %>
<%@ page import="java.util.List" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <title>Area Checker</title>
    <link rel="preconnect" href="https://fonts.googleapis.com" />
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
    <link href="https://fonts.googleapis.com/css2?family=Press+Start+2P&display=swap" rel="stylesheet" />
    <link rel="stylesheet" href="styles/main.css" />
</head>
<body>
    <div class="container">
        <header class="header">
            <h1><a href="/">Area Checker</a></h1>
            <p>Ильин Никита | P3210 | 408698 | Вар. 488778</p>
        </header>
        <form id="areaCheckForm" action="" method="get">
            <div class="controls">
                <div>
                    <label for="x">X:</label>
                    <select name="x" id="x" required>
                        <option value="-4">-4</option>
                        <option value="-3">-3</option>
                        <option value="-2">-2</option>
                        <option value="-1">-1</option>
                        <option value="0">0</option>
                        <option value="1">1</option>
                        <option value="2">2</option>
                        <option value="3">3</option>
                        <option value="4">4</option>
                    </select>
                </div>
                <div>
                    <label for="y">Y:</label>
                    <input name="y" type="number" id="y" step="any" max="5" min="-3" value="0" required />
                </div>
                <div class="checkbox-group">
                    <label>R:</label>
                    <label><input name="R" type="radio" value="1" required />1</label>
                    <label><input name="R" type="radio" value="2" required />2</label>
                    <label><input name="R" type="radio" value="3" required />3</label>
                    <label><input name="R" type="radio" value="4" required />4</label>
                    <label><input name="R" type="radio" value="5" required />5</label>
                </div>
                <button type="submit">Check Point</button>
            </div>
        </form>
        <canvas id="canvas" width="400" height="400"></canvas>
        <div id="result" class="result"></div>
        <table id="resultsTable">
            <thead>
                <tr>
                    <th>№</th>
                    <th>X</th>
                    <th>Y</th>
                    <th>R</th>
                    <th>Result</th>
                    <th>Curr.time</th>
                    <th>Exec.time</th>
                </tr>
            </thead>
            <tbody id="resultsBody">
                <%
                    List<CheckResult> results = (List<CheckResult>) request.getAttribute("results");
                    if (results != null) {
                        for (int i = 0; i < results.size(); i++) {
                            CheckResult result = results.get(i);
                %>
                    <tr>
                        <td><%= result.getId() %></td>
                        <td><%= result.getX() %></td>
                        <td><%= result.getY() %></td>
                        <td><%= result.getR() %></td>
                        <td><%= result.isHit() ? "Inside" : "Outside" %></td>
                        <td><%= result.getTime() %></td>
                        <td><%= result.getExecutionTime() %></td>
                    </tr>
                <%     }
                    }
                %>
            </tbody>
        </table>
        <div id="floatingMessage" class="floating-message" style="display: none;">Error message</div>
    </div>
    <script>
        const canvas = document.getElementById("canvas");
        const ctx = canvas.getContext("2d");
        const width = canvas.width;
        const height = canvas.height;

        function drawCoordinateSystem(R) {
            ctx.clearRect(0, 0, width, height);
            const scale = width / (4 * R);

            // Draw axes
            ctx.beginPath();
            ctx.moveTo(0, height / 2);
            ctx.lineTo(width, height / 2);
            ctx.moveTo(width / 2, 0);
            ctx.lineTo(width / 2, height);
            ctx.stroke();

            // Draw R and R/2 marks
            ctx.textAlign = "center";
            ctx.textBaseline = "middle";
            ctx.fillStyle = "#000000";
            const markings = [-R, -R / 2, R / 2, R];

            markings.forEach((mark) => {
                const x = width / 2 + mark * scale;
                const y = height / 2 - mark * scale;

                // X-axis markings
                ctx.beginPath();
                ctx.moveTo(x, height / 2 - 5);
                ctx.lineTo(x, height / 2 + 5);
                ctx.stroke();
                ctx.fillText(mark === 0 ? "" : mark.toString(), x, height / 2 + 20);

                // Y-axis markings
                ctx.beginPath();
                ctx.moveTo(width / 2 - 5, y);
                ctx.lineTo(width / 2 + 5, y);
                ctx.stroke();
                ctx.fillText(mark === 0 ? "" : mark.toString(), width / 2 - 20, y);
            });

            // Draw axis labels
            ctx.fillText("X", width - 10, height / 2 - 20);
            ctx.fillText("Y", width / 2 + 20, 10);
        }

        function drawAreas(R) {
            const scale = width / (4 * R);

            // Rectangle
            ctx.fillStyle = "rgba(0, 0, 255, 0.3)";
            ctx.fillRect(width / 2 - R * scale, height / 2 - (R / 2) * scale, R * scale, (R / 2) * scale);

            // Triangle
            ctx.beginPath();
            ctx.moveTo(width / 2, height / 2);
            ctx.lineTo(width / 2 + R * scale, height / 2);
            ctx.lineTo(width / 2, height / 2 + (R / 2) * scale);
            ctx.closePath();
            ctx.fill();

            // Quarter circle
            ctx.beginPath();
            ctx.moveTo(width / 2, height / 2);
            ctx.arc(width / 2, height / 2, R * scale, -Math.PI / 2, 0);
            ctx.closePath();
            ctx.fill();
        }

        function updateCanvas() {
            const r = document.querySelector('input[name="R"]:checked').value;
            drawCoordinateSystem(r);
            drawAreas(r);
        }

        const radioButtons = document.querySelectorAll('input[name="R"]');
        radioButtons.forEach(radio => {
            radio.addEventListener('change', updateCanvas);
        });

        let messageTimeout;
        function showFloatingMessage(message) {
            const messageDiv = document.getElementById("floatingMessage");
            messageDiv.textContent = message;
            messageDiv.style.display = "block";
            
            if (messageTimeout) {
                clearTimeout(messageTimeout);
            }
            
            messageTimeout = setTimeout(() => {
                messageDiv.style.display = "none";
            }, 1500);
        }

        function limitYInput() {
            const yInput = document.getElementById("y");
            yInput.addEventListener("input", function () {
                let value = parseFloat(this.value);
                if (value > 5) {
                    this.value = 5;
                } else if (value < -3) {
                    this.value = -3;
                }
                const valueStr = this.value;
                const decimalIndex = valueStr.indexOf(".");
                if (decimalIndex !== -1) {
                    const floatPart = valueStr.substring(decimalIndex + 1);
                    const maxLength = 15; // maximum length of the floating part

                    if (floatPart.length > maxLength) {
                        this.value = parseFloat(valueStr).toFixed(maxLength);
                    }
                }
            });
        }
        const allowedXValues = [-4, -3, -2, -1, 0, 1, 2, 3, 4];
        function findNearestX(value) {
            return allowedXValues.reduce((prev, curr) => 
                Math.abs(curr - value) < Math.abs(prev - value) ? curr : prev
            );
        }


        canvas.addEventListener('click', function(event) {
            const rect = canvas.getBoundingClientRect();
            const checkedR = document.querySelector('input[name="R"]:checked');
            if (checkedR == null){
                showFloatingMessage('You have to choose R first.');
                return;
            }
            const r = checkedR.value;
            let x = ((event.clientX - rect.left) / width) * (4 * r) - 2*r; 
            const y = -((event.clientY - rect.top) / height) * (4 * r) + 2*r; 

            x = findNearestX(x);

            submitForm(x, y, r);
        });

        function submitForm(x, y, r) {
            document.getElementById('x').value = x;
            document.getElementById('y').value = y.toFixed(2);
            document.getElementById("areaCheckForm").submit();
        }


        function drawPoint(x, y, R) {
            const scale = width / (4 * R);
            ctx.fillStyle = "red";
            ctx.beginPath();
            ctx.arc(width / 2 + x * scale, height / 2 - y * scale, 3, 0, 2 * Math.PI);
            ctx.fill();
        }

        // Initial setup
        limitYInput();

        <%
            if (results != null && !results.isEmpty()) {
                CheckResult lastResult = results.get(results.size() - 1);
        %>
                drawCoordinateSystem(<%= lastResult.getR() %>);
                drawAreas(<%= lastResult.getR() %>);
                drawPoint(<%= lastResult.getX() %>, <%= lastResult.getY() %>, <%= lastResult.getR() %>);
        <%
            } else {
        %>
                drawCoordinateSystem(3);
                drawAreas(3);
        <%
            }
        %>

        <%
            String error = (String) request.getAttribute("error");
            if (error != null) {
                String errorMessage = error;
        %>
                showFloatingMessage("<%= errorMessage %>");
        <%
            }
        %>

    </script>
</body>
</html>
