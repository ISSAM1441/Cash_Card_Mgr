-- 1. Create Table with Postgres-native types
DO
$$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM ${schema_name}.CASH_CARD
        WHERE ID = '3b926f79-bd2d-4cd5-b3e5-be3101ccd0c3'
    ) THEN
        EXECUTE format(
            'CREATE TABLE  IF NOT EXISTS ${schema_name}.CASH_CARD',
            '(',
                 'ID                  UUID          NOT NULL,',
                 'AMOUNT              NUMERIC(10,2) NOT NULL DEFAULT 0.00',
            ');'

             -- 2. Establish Primary Key
             'ALTER TABLE ${schema_name}.CASH_CARD',
                 'ADD CONSTRAINT CASH_CARD_PK PRIMARY KEY (ID);',

             -- 3. Assign Role Permissions
             'GRANT SELECT, INSERT, UPDATE, DELETE ON ${schema_name}.CASH_CARD TO ${role-rw};',

             -- 4. Seed Data (Fixed quote formatting)
             'INSERT INTO ${schema_name}.CASH_CARD (ID, AMOUNT)',
             'VALUES (''3b926f79-bd2d-4cd5-b3e5-be3101ccd0c3'', 123.45);'
            );
        END IF;
    END
$$;


