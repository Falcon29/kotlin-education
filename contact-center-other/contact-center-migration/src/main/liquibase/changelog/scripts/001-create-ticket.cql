CREATE TABLE IF NOT EXISTS cc_tickets (
    id text PRIMARY KEY,
    title text,
    description text,
    client_id text,
    operator_id text,
    status text,
    priority text,
    permissions text,
    deal_side text,
    lock text
);

CREATE CUSTOM INDEX IF NOT EXISTS ON cc_tickets (title)
  USING 'org.apache.cassandra.index.sasi.SASIIndex'
  WITH OPTIONS = {
    'analyzer_class': 'org.apache.cassandra.index.sasi.analyzer.StandardAnalyzer',
    'tokenization_enable_stemming': 'true',
    'analyzed': 'true',
    'tokenization_normalize_lowercase': 'true',
    'tokenization_locale': 'en'
  };
