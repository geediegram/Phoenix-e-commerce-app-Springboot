
set foreign_key_checks = 0;

truncate table product;

insert into product(id, name, price, quantity)
values(12, 'Luxury Mop', 2340, 3),
    (13, 'Demola', 40, 4),
    (14, 'Toye', 10, 5),
    (15, 'Tife', 23, 6);

set foreign_key_checks = 1;