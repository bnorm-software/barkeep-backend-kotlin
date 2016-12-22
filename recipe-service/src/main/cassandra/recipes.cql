CREATE TYPE component (
  ingredients list<uuid>,
  min int,
  max int,
)

CREATE TABLE barkeep.recipes (
  id uuid PRIMARY KEY,

  name text,
  description text,
  instructions list<text>,

  owner uuid,
  image uuid,
  components list<component>,
  books set<uuid>,
);
