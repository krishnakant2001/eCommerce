INSERT INTO orders (total_price, order_status) VALUES
(150.99, 'PENDING'),
(250.76, 'CONFIRMED'),
(320.00, 'CONFIRMED'),
(450.50, 'PENDING'),
(600.56, 'CONFIRMED'),
(120.00, 'CANCELLED'),
(980.00, 'CONFIRMED'),
(75.99, 'CANCELLED'),
(430.00, 'PENDING'),
(210.00, 'CONFIRMED'),
(560.50, 'CONFIRMED'),
(300.40, 'PENDING'),
(890.99, 'CONFIRMED'),
(160.30, 'CONFIRMED'),
(1020.00, 'PENDING');


INSERT INTO order_item (order_id, product_id, quantity) VALUES
(1, 101, 2),
(1, 102, 3),

(2, 103, 1),
(2, 104, 4),

(3, 105, 2),
(3, 106, 1),

(4, 107, 3),
(4, 108, 2),

(5, 109, 1),
(5, 110, 2),

(6, 111, 1),

(7, 112, 4),
(7, 113, 1),

(8, 114, 1),

(9, 115, 2),
(9, 101, 3),

(10, 102, 2),

(11, 103, 5),

(12, 104, 2),

(13, 105, 4),

(14, 106, 2),

(15, 107, 3);
