CREATE TABLE IF NOT EXISTS app_users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    display_name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_app_users_email UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS recipes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    author_id BIGINT NOT NULL,
    title VARCHAR(150) NOT NULL,
    short_description VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_recipes_author FOREIGN KEY (author_id) REFERENCES app_users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS recipe_ingredients (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    recipe_id BIGINT NOT NULL,
    sort_order INT NOT NULL,
    ingredient_text VARCHAR(255) NOT NULL,
    CONSTRAINT fk_recipe_ingredients_recipe FOREIGN KEY (recipe_id) REFERENCES recipes(id) ON DELETE CASCADE,
    CONSTRAINT uq_recipe_ingredient_order UNIQUE (recipe_id, sort_order)
);

CREATE TABLE IF NOT EXISTS recipe_steps (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    recipe_id BIGINT NOT NULL,
    sort_order INT NOT NULL,
    step_text TEXT NOT NULL,
    CONSTRAINT fk_recipe_steps_recipe FOREIGN KEY (recipe_id) REFERENCES recipes(id) ON DELETE CASCADE,
    CONSTRAINT uq_recipe_step_order UNIQUE (recipe_id, sort_order)
);

CREATE TABLE IF NOT EXISTS recipe_reactions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    recipe_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    reaction_type VARCHAR(10) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_recipe_reactions_recipe FOREIGN KEY (recipe_id) REFERENCES recipes(id) ON DELETE CASCADE,
    CONSTRAINT fk_recipe_reactions_user FOREIGN KEY (user_id) REFERENCES app_users(id) ON DELETE CASCADE,
    CONSTRAINT uq_recipe_user_reaction UNIQUE (recipe_id, user_id),
    CONSTRAINT chk_recipe_reaction_type CHECK (reaction_type IN ('LIKE', 'DISLIKE'))
);

CREATE INDEX idx_recipes_title ON recipes (title);
CREATE INDEX idx_recipe_reactions_recipe ON recipe_reactions (recipe_id);
CREATE INDEX idx_recipe_reactions_user ON recipe_reactions (user_id);
