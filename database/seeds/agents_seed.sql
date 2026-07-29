-- AgentMesh Seeds for 5 Microservice AI Agents with Valid 58-character Algorand Wallet Addresses

INSERT INTO agents (id, name, endpoint, wallet_address, rating, success_rate, health_status, base_price, supported_capabilities)
VALUES 
(
    'agent-research-01',
    'Research & Market Intelligence Agent',
    'http://localhost:8001',
    'D64EJWVXUFY3SRUNHXL6XZHPMHXVQFBOFX723TVNAINBGG6MJLWWZOHKPQ',
    4.9,
    98.5,
    'UP',
    45.0,
    'RESEARCH,MARKET_ANALYSIS,COMPETITOR_RESEARCH,SUMMARY'
),
(
    'agent-code-02',
    'Full-Stack Coding & Architecture Agent',
    'http://localhost:8002',
    'XU4URLGPIYXCXPXYHBTHGLWPLEZOP2F3D7OM2VSRTWK4QEKTKRF6T74KJI',
    4.8,
    96.0,
    'UP',
    80.0,
    'FRONTEND,BACKEND,DEVELOPMENT,REACT,SPRING_BOOT,API_DESIGN'
),
(
    'agent-image-03',
    'Brand & Visual Graphics Agent',
    'http://localhost:8003',
    'KVYGHYDZ4GGDUD4KZ555XRUGG7GHBJQT3FWCNHE47E2PCDSUY54XOIHZ2U',
    4.95,
    99.0,
    'UP',
    60.0,
    'LOGO_DESIGN,BRANDING,UI_UX,GRAPHICS,SVG_GENERATION'
),
(
    'agent-ppt-04',
    'Pitch Deck & Strategy Agent',
    'http://localhost:8004',
    '5BJXBTQPXI6MAPHJF2YHTPABUAEM5ZDGEZWSBN5OQXQWQ67HVW47OUIUOU',
    4.75,
    94.5,
    'UP',
    55.0,
    'PRESENTATION,PITCH_DECK,BUSINESS_PLAN,SLIDE_GENERATION'
),
(
    'agent-testing-05',
    'Automated QA & Security Agent',
    'http://localhost:8005',
    'MB3R5YONVGOARERGS2O2FAQ5MXRIZOKPFCGALD5DP7BJWFSKO3ZDUBLNRQ',
    4.85,
    97.2,
    'UP',
    35.0,
    'TESTING,QA,CODE_AUDIT,SECURITY_CHECK,VALIDATION'
)
ON CONFLICT (id) DO UPDATE SET
    endpoint = EXCLUDED.endpoint,
    wallet_address = EXCLUDED.wallet_address,
    rating = EXCLUDED.rating,
    success_rate = EXCLUDED.success_rate,
    health_status = EXCLUDED.health_status,
    base_price = EXCLUDED.base_price,
    supported_capabilities = EXCLUDED.supported_capabilities;
