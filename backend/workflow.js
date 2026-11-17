
/**
 * AURA AI — Workflow Engine
 * Mengatur proses produksi affiliate secara berurutan.
 */

export const STAGES = {
  QUEUED: "queued",
  RESEARCHING: "researching",
  IMAGE_GENERATION: "image_generation",
  AWAITING_IMAGE_APPROVAL: "awaiting_image_approval",
  VIDEO_GENERATION: "video_generation",
  SAVING: "saving",
  COMPLETED: "completed",
  FAILED: "failed"
};

const NEXT_STAGE = {
  queued: STAGES.RESEARCHING,
  researching: STAGES.IMAGE_GENERATION,
  image_generation: STAGES.AWAITING_IMAGE_APPROVAL,
  video_generation: STAGES.SAVING,
  saving: STAGES.COMPLETED
};

export function createWorkflow(products = []) {
  return {
    id: `aura-${Date.now()}`,
    status: products.length ? "running" : "idle",
    currentProductIndex: 0,
    products: products.map((product, index) => ({
      id: String(product.id || `product-${index + 1}`),
      name: String(product.name || "").trim(),
      sourceUrl: product.sourceUrl || null,
      status: STAGES.QUEUED,
      imageApproved: false,
      imageAttempts: 0,
      videoUrl: null,
      error: null
    })),
    createdAt: new Date().toISOString(),
    updatedAt: new Date().toISOString()
  };
}

export function currentProduct(workflow) {
  return workflow.products[workflow.currentProductIndex] || null;
}

export function advanceWorkflow(workflow) {
  const product = currentProduct(workflow);

  if (!product) {
    return { ...workflow, status: "completed" };
  }

  if (product.status === STAGES.AWAITING_IMAGE_APPROVAL) {
    throw new Error("Menunggu persetujuan gambar.");
  }

  const next = NEXT_STAGE[product.status];

  if (!next) {
    throw new Error("Tahapan tidak dapat dilanjutkan.");
  }

  const updated = {
    ...product,
    status: next,
    error: null
  };

  if (next === STAGES.IMAGE_GENERATION) {
    updated.imageAttempts += 1;
  }

  const products = workflow.products.map((item, index) =>
    index === workflow.currentProductIndex ? updated : item
  );

  return {
    ...workflow,
    products,
    updatedAt: new Date().toISOString()
  };
}

export function approveImage(workflow, approved) {
  const product = currentProduct(workflow);

  if (!product || product.status !== STAGES.AWAITING_IMAGE_APPROVAL) {
    throw new Error("Tidak ada gambar yang menunggu persetujuan.");
  }

  const updated = approved
    ? {
        ...product,
        imageApproved: true,
        status: STAGES.VIDEO_GENERATION
      }
    : {
        ...product,
        imageApproved: false,
        status: STAGES.IMAGE_GENERATION,
        imageAttempts: product.imageAttempts + 1
      };

  const products = workflow.products.map((item, index) =>
    index === workflow.currentProductIndex ? updated : item
  );

  return {
    ...workflow,
    products,
    updatedAt: new Date().toISOString()
  };
}

export function finishCurrentProduct(workflow, videoUrl) {
  const product = currentProduct(workflow);

  if (!product || product.status !== STAGES.SAVING) {
    throw new Error("Produk belum siap disimpan.");
  }

  if (!product.imageApproved) {
    throw new Error("Gambar belum disetujui.");
  }

  const products = workflow.products.map((item, index) =>
    index === workflow.currentProductIndex
      ? {
          ...item,
          status: STAGES.COMPLETED,
          videoUrl: videoUrl || null
        }
      : item
  );

  const nextIndex = workflow.currentProductIndex + 1;
  const hasNext = nextIndex < products.length;

  return {
    ...workflow,
    products,
    currentProductIndex: hasNext
      ? nextIndex
      : workflow.currentProductIndex,
    status: hasNext ? "running" : "completed",
    updatedAt: new Date().toISOString()
  };
}
