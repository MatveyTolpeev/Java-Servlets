<%@ page import="java.util.ArrayList" %>
<%@ page import="ru.service.db.models.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<html>
<head>
    <title>Title</title>
        <style>
        html {
          font-family: 'helvetica neue', helvetica, arial, sans-serif;
        }

        body {
         background-image: url("https://kolesa-uploads.ru/-/d5f496b6-436d-4bd7-b4e1-49acc3c4491c/gl.jpg");
         background-color: #cccccc;
        }

        thead th, tfoot th {
          font-family: 'Rock Salt', cursive;
        }

        th {
          letter-spacing: 2px;
          background-color: #ffff;
        }

        td {
          letter-spacing: 1px;
          background-color: #ffff;
        }

        tbody td {
          text-align: center;
        }

        tfoot th {
          text-align: right;
        }

        thead, tfoot {
          background: url(leopardskin.jpg);
          color: white;
          text-shadow: 1px 1px 1px black;
        }

        thead th, tfoot th, tfoot td {
          background: linear-gradient(to bottom, rgba(0,0,0,0.1), rgba(0,0,0,0.5));
          border: 3px solid purple;
        }

        tbody tr:nth-child(odd) {
          background-color: #ff33cc;
        }

        tbody tr:nth-child(even) {
          background-color: #e495e4;
        }

        tbody tr {
            background-color: #ffff;
        }

        table {
          background-color: #ff33cc;
        }

        caption {
          font-family: 'Rock Salt', cursive;
          padding: 20px;
          font-style: italic;
          caption-side: bottom;
          color: #666;
          text-align: center;
          letter-spacing: 1px;
        }


        .form-style-2 {
                max-width: 500px;
                padding: 20px 12px 10px 20px;
                font: 13px Arial, Helvetica, sans-serif;
            }

            .form-style-2-heading {
                font-weight: bold;
                font-style: italic;
                border-bottom: 2px solid #ddd;
                margin-bottom: 20px;
                font-size: 15px;
                padding-bottom: 3px;
            }
        </style>
</head>
<body>

<div class="form-style-2">
    <div class="form-style-2-heading">
        All orders:
    </div>
    <table align="center">
        <tr>
            <th>Order id</th>
            <th>Transport id</th>
            <th>Client id</th>
            <th>Employee id</th>
            <th>Service id</th>
            <th>Start date</th>
            <th>Plan end date</th>
        </tr>
        <c:forEach items="${ordersFromServer}" var="order">
            <tr>
                <td>${order.orderId}</td>
                <td>${order.transportId}</td>
                <td>${order.clientId}</td>
                <td>${order.employeeId}</td>
                <td>${order.serviceId}</td>
                <td>${order.startDate}</td>
                <td>${order.planningEndDate}</td>
            </tr>
        </c:forEach>
    </table>
</div>
</body>
</html>
