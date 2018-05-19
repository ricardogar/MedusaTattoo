CREATE TRIGGER update_insert_total_pagado
AFTER INSERT
   ON pago FOR EACH ROW
UPDATE trabajo t SET t.total_pagado=(select cast(sum(p.valor)as char) from pago p where p.trabajo_id=NEW.trabajo_id) WHERE id=NEW.trabajo_id;

CREATE TRIGGER update_delete_total_pagado
AFTER delete
   ON pago FOR EACH ROW
UPDATE trabajo t SET t.total_pagado=(select cast(sum(p.valor)as char) from pago p where p.trabajo_id=OLD.trabajo_id) WHERE id=OLD.trabajo_id;

CREATE TRIGGER update_update_total_pagado
AFTER update
   ON pago FOR EACH ROW
UPDATE trabajo t SET t.total_pagado=(select cast(sum(p.valor)as char) from pago p where p.trabajo_id=NEW.trabajo_id) WHERE id=NEW.trabajo_id;