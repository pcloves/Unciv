// Run: node lessons/assets/check-course.cjs [playwright-module-path] [existing-output-dir]
const assert = require("node:assert/strict");
const fs = require("node:fs");
const path = require("node:path");
const { pathToFileURL, fileURLToPath } = require("node:url");
const { chromium } = require(process.argv[2] || "playwright");

const course = path.resolve(__dirname, "../unciv-hex-strategy");
const repo = path.resolve(__dirname, "../..");
const output = process.argv[3];
if (output) assert(fs.statSync(output).isDirectory());
const pages = ["index.html", "0001-runtime-map.html", "0002-data-ownership.html", "0003-rule-pipeline.html", "0004-reading-with-evidence.html", "0005-hex-coordinates.html", "0006-tilemap.html", "0007-map-generation.html", "0008-landmass-climate.html", "0009-elevation-rivers.html", "0010-starts-resources.html", "0011-movement-pathing.html", "0012-sight-fog.html", "0013-tile-yields.html", "0014-population-growth.html", "0015-production-construction.html", "0016-civilization-economy.html", "reference/architecture.html", "reference/hexmath.html"];
const quizCounts = { "0001-runtime-map.html": 4, "0002-data-ownership.html": 4, "0003-rule-pipeline.html": 4, "0004-reading-with-evidence.html": 4, "0005-hex-coordinates.html": 4, "0006-tilemap.html": 4, "0007-map-generation.html": 4, "0008-landmass-climate.html": 4, "0009-elevation-rivers.html": 4, "0010-starts-resources.html": 4, "0011-movement-pathing.html": 4, "0012-sight-fog.html": 4, "0013-tile-yields.html": 4, "0014-population-growth.html": 4, "0015-production-construction.html": 4, "0016-civilization-economy.html": 4 };

(async () => {
  const browser = await chromium.launch({ channel: "msedge", headless: true });
  try {
    const page = await browser.newPage();
    const errors = [];
    page.on("pageerror", (error) => errors.push(error.message));
    for (const file of pages) {
      await page.goto(pathToFileURL(path.join(course, file)).href);
      assert.equal(await page.locator("h1").count(), 1, file);
      const pairs = await page.locator("[data-pair-check]").evaluateAll((nodes) =>
        nodes.map((node) => ({
          code: node.querySelector("pre code").textContent,
          explanation: node.children[1].textContent,
        })));
      if (file === "0015-production-construction.html") {
        assert.equal(pairs.length, 10, "Lesson 15 must check all 10 code/explanation pairs");
        assert.equal(await page.locator(".paired:has(pre)").count(), pairs.length);
      }
      if (file === "0016-civilization-economy.html") {
        assert.equal(pairs.length, 9, "Lesson 16 must check all 9 code/explanation pairs");
        assert.equal(await page.locator(".paired:has(pre)").count(), pairs.length);
      }
      for (const pair of pairs) {
        const left = pair.code.match(/[①-⑳]/g) || [];
        const right = pair.explanation.match(/[①-⑳]/g) || [];
        assert(left.length > 0, `Missing code numbering: ${file}`);
        assert.deepEqual(left, right, `Code/explanation numbering mismatch: ${file}`);
        assert.equal(left.join(""), "①②③④⑤⑥⑦⑧⑨⑩⑪⑫⑬⑭⑮⑯⑰⑱⑲⑳".slice(0, left.length),
          `Missing or repeated number: ${file}`);
        for (const line of pair.code.split("\n")) {
          const width = [...line].reduce((sum, char) => sum + (char.codePointAt(0) > 127 ? 2 : 1), 0);
          assert(width <= 100, `Code line exceeds 100 columns: ${file}: ${line}`);
        }
      }
      const links = await page.locator("[href], [src]").evaluateAll((nodes) =>
        nodes.map((node) => node.href || node.src));
      for (const link of links) {
        const url = new URL(link);
        if (url.protocol === "file:") {
          const target = fileURLToPath(url);
          assert(fs.existsSync(target), `Missing local target: ${link}`);
          if (url.hash && target.endsWith(".html")) {
            const found = await page.evaluate(({ html, id }) =>
              new DOMParser().parseFromString(html, "text/html").getElementById(id) !== null,
            { html: fs.readFileSync(target, "utf8"), id: decodeURIComponent(url.hash.slice(1)) });
            assert(found, `Missing anchor: ${link}`);
          }
        } else if (url.hostname === "github.com" && url.pathname.includes("/blob/")) {
          if (file === "0015-production-construction.html") {
            assert.equal(url.pathname.split("/")[4], "1df6249a41301056ddc4448a95a93ddf5403e061",
              `Unexpected source revision: ${link}`);
          }
           const source = path.join(repo, decodeURIComponent(url.pathname.split("/").slice(5).join("/")));
          assert(fs.existsSync(source), `Missing cited source: ${link}`);
          if (/^#L\d+$/.test(url.hash)) {
            assert(Number(url.hash.slice(2)) <= fs.readFileSync(source, "utf8").split("\n").length);
          }
        }
      }
      for (const viewport of [
        { width: 2560, height: 1440 },
        { width: 1536, height: 864 },
        { width: 390, height: 844 },
      ]) {
        await page.setViewportSize(viewport);
        assert(await page.evaluate(() => document.documentElement.scrollWidth <= innerWidth + 1),
          `Horizontal page overflow: ${file} at ${viewport.width}`);
        if (["0015-production-construction.html", "0016-civilization-economy.html"].includes(file) && viewport.width >= 1536) {
          assert(await page.locator("pre").evaluateAll((nodes) =>
            nodes.every((node) => node.scrollWidth <= node.clientWidth + 1)),
          `Horizontal code overflow: ${file} at ${viewport.width}`);
        }
        assert(await page.locator("img").evaluateAll((images) =>
          images.every((image) => image.complete && image.naturalWidth > 0)), `Missing image: ${file}`);
        if (output && viewport.width !== 1536) {
          await page.screenshot({ path: path.join(output, `${path.basename(file, ".html")}-${viewport.width}.png`) });
        }
      }
      if (output && file.startsWith("reference/")) {
        await page.pdf({ path: path.join(output, `${path.basename(file, ".html")}-print.pdf`), printBackground: true, preferCSSPageSize: true });
      }
    }

    for (const [file, expectedForms] of Object.entries(quizCounts)) {
      await page.goto(pathToFileURL(path.join(course, file)).href);
      await page.setViewportSize({ width: 2560, height: 1440 });
      assert(await page.evaluate(() =>
        Boolean(document.querySelector("#mental").compareDocumentPosition(document.querySelector("#practice"))
          & Node.DOCUMENT_POSITION_FOLLOWING)), file);
      const forms = page.locator("form.quiz");
      assert.equal(await forms.count(), expectedForms, file);
      assert.equal(await page.locator("button").count(), expectedForms, `Orphan quiz button: ${file}`);
      assert.equal(await page.locator("output").count(), expectedForms, `Orphan quiz output: ${file}`);
      for (const form of await forms.all()) {
        const feedback = form.locator(".feedback");
        assert.equal(await form.locator("fieldset legend").count(), 1);
        assert.equal(await form.locator("button").count(), 1);
        assert.equal(await feedback.count(), 1);
        assert.equal(await form.locator('input[type="radio"]').count(), 4);
        assert(await form.locator('input[type="radio"][required]').count() > 0);
        await form.locator("button").click();
        assert.equal(await feedback.textContent(), "", "Unanswered form must not be graded");
        const correct = await form.getAttribute("data-answer");
        assert.equal(await form.locator(`input[value="${correct}"]`).count(), 1);
        for (const wrong of await form.locator(`input:not([value="${correct}"])`).all()) {
          const message = await wrong.getAttribute("data-feedback");
          assert(message && message.trim(), `Missing option feedback: ${file}`);
          await wrong.check();
          await form.locator("button").click();
          assert.equal(await feedback.getAttribute("data-result"), "incorrect");
          assert.equal(await feedback.textContent(), "再想一想。" + message);
        }
        await form.locator(`input[value="${correct}"]`).focus();
        await page.keyboard.press("Space");
        assert.equal(await feedback.textContent(), "", "Selection changes must clear old feedback");
        await form.locator("button").click();
        assert.equal(await feedback.getAttribute("data-result"), "correct");
        assert((await feedback.textContent()).startsWith("正确。"));
      }

      const details = page.locator("details");
      await details.first().locator("summary").click();
      const initial = await details.evaluateAll((nodes) => nodes.map((node) => node.open));
      await page.evaluate(() => { dispatchEvent(new Event("beforeprint")); dispatchEvent(new Event("beforeprint")); });
      assert(await details.evaluateAll((nodes) => nodes.every((node) => node.open)));
      await page.evaluate(() => dispatchEvent(new Event("afterprint")));
      assert.deepEqual(await details.evaluateAll((nodes) => nodes.map((node) => node.open)), initial);
      if (output) {
        await page.pdf({ path: path.join(output, `${path.basename(file, ".html")}-print.pdf`), printBackground: true, preferCSSPageSize: true });
      }
    }
    await page.goto(pathToFileURL(path.join(course, "0001-runtime-map.html")).href);
    if (output) {
      await page.locator("#snapshot").screenshot({ path: path.join(output, "lesson-code.png") });
    }
    await page.goto(pathToFileURL(path.join(course, "0002-data-ownership.html")).href);
    if (output) {
      await page.locator("#coords").screenshot({ path: path.join(output, "lesson2-code.png") });
    }
    await page.goto(pathToFileURL(path.join(course, "0003-rule-pipeline.html")).href);
    if (output) {
      await page.locator("#use").screenshot({ path: path.join(output, "lesson3-code.png") });
    }
    await page.goto(pathToFileURL(path.join(course, "0004-reading-with-evidence.html")).href);
    if (output) {
      await page.locator("#testgame").screenshot({ path: path.join(output, "lesson4-code.png") });
    }
    await page.goto(pathToFileURL(path.join(course, "0005-hex-coordinates.html")).href);
    if (output) {
      await page.locator("#axes").screenshot({ path: path.join(output, "lesson5-code.png") });
    }
    await page.goto(pathToFileURL(path.join(course, "0006-tilemap.html")).href);
    if (output) {
      await page.locator("#storage").screenshot({ path: path.join(output, "lesson6-code.png") });
    }
    await page.goto(pathToFileURL(path.join(course, "0007-map-generation.html")).href);
    if (output) {
      await page.locator("#stages").screenshot({ path: path.join(output, "lesson7-code.png") });
    }
    await page.goto(pathToFileURL(path.join(course, "0008-landmass-climate.html")).href);
    if (output) {
      await page.locator("#climate").screenshot({ path: path.join(output, "lesson8-code.png") });
    }
    await page.goto(pathToFileURL(path.join(course, "0009-elevation-rivers.html")).href);
    if (output) {
      await page.locator("#elevation").screenshot({ path: path.join(output, "lesson9-code.png") });
    }
    await page.goto(pathToFileURL(path.join(course, "0010-starts-resources.html")).href);
    if (output) {
      await page.locator("#regions").screenshot({ path: path.join(output, "lesson10-code.png") });
    }
    await page.goto(pathToFileURL(path.join(course, "0011-movement-pathing.html")).href);
    if (output) {
      await page.locator("#cost").screenshot({ path: path.join(output, "lesson11-code.png") });
    }
    await page.goto(pathToFileURL(path.join(course, "0012-sight-fog.html")).href);
    if (output) {
      await page.locator("#sight").screenshot({ path: path.join(output, "lesson12-code.png") });
    }
    await page.goto(pathToFileURL(path.join(course, "0013-tile-yields.html")).href);
    if (output) {
      await page.locator("#receipt").screenshot({ path: path.join(output, "lesson13-code.png") });
    }
    await page.goto(pathToFileURL(path.join(course, "0014-population-growth.html")).href);
    if (output) {
      await page.locator("#ownership").screenshot({ path: path.join(output, "lesson14-code.png") });
    }
    await page.goto(pathToFileURL(path.join(course, "0015-production-construction.html")).href);
    if (output) {
      await page.locator("#overflow").screenshot({ path: path.join(output, "lesson15-code.png") });
    }
    await page.goto(pathToFileURL(path.join(course, "0016-civilization-economy.html")).href);
    if (output) {
      await page.locator("#economy-states").screenshot({ path: path.join(output, "lesson16-code.png") });
    }
    await page.emulateMedia({ media: "print" });
    assert.equal(await page.locator(".toc").evaluate((node) => getComputedStyle(node).display), "none");
    assert.deepEqual(errors, []);
    console.log(`PASS: ${pages.length} pages; local links, anchors and source citations; 3 viewport sizes; images; ${Object.values(quizCounts).reduce((sum, count) => sum + count, 0)} quizzes (all incorrect options and keyboard correction); lessons 15-16 code/explanation numbering and line widths; no orphan quiz controls; print disclosure restoration.`);
  } finally {
    await browser.close();
  }
})().catch((error) => { console.error(error); process.exitCode = 1; });
